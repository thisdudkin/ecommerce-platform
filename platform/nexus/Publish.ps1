[CmdletBinding()]
param(
    [string]$Username = "admin",
    [string]$Password
)

$ErrorActionPreference = "Stop"
$Root = (Resolve-Path "$PSScriptRoot\..\..").Path
$Project = Join-Path $Root "platform\security\pom.xml"
$Settings = Join-Path $PSScriptRoot "maven-settings.xml"

docker compose --file (Join-Path $Root "compose.yml") up --detach --wait nexus
if ($LASTEXITCODE -ne 0) {
    throw "Nexus cannot start"
}
if ([string]::IsNullOrWhiteSpace($Password)) {
    $Password = (docker exec nexus cat /nexus-data/admin.password).Trim()
}
if ([string]::IsNullOrWhiteSpace($Password)) {
    throw "Nexus credentials cannot be resolved"
}

$Token = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${Username}:$Password"))
$Headers = @{ Authorization = "Basic $Token" }
$Eula = Invoke-RestMethod -Uri "http://localhost:8081/service/rest/v1/system/eula" -Headers $Headers
if (-not $Eula.accepted) {
    $Eula.accepted = $true
    Invoke-RestMethod -Method Post -Uri "http://localhost:8081/service/rest/v1/system/eula" -Headers $Headers -ContentType "application/json" -Body ($Eula | ConvertTo-Json)
}
$Anonymous = @{ enabled = $true; userId = "anonymous"; realmName = "NexusAuthorizingRealm" } | ConvertTo-Json
Invoke-RestMethod -Method Put -Uri "http://localhost:8081/service/rest/v1/security/anonymous" -Headers $Headers -ContentType "application/json" -Body $Anonymous
$Repository = Invoke-RestMethod -Uri "http://localhost:8081/service/rest/v1/repositories/maven/hosted/maven-releases" -Headers $Headers
$Repository.storage.writePolicy = "ALLOW"
$Configuration = $Repository | Select-Object name, online, storage, cleanup, maven, component | ConvertTo-Json -Depth 5
Invoke-RestMethod -Method Put -Uri "http://localhost:8081/service/rest/v1/repositories/maven/hosted/maven-releases" -Headers $Headers -ContentType "application/json" -Body $Configuration

$PreviousUsername = $env:NEXUS_USERNAME
$PreviousPassword = $env:NEXUS_PASSWORD
try {
    $env:NEXUS_USERNAME = $Username
    $env:NEXUS_PASSWORD = $Password
    & mvn --batch-mode --no-transfer-progress --settings $Settings --file $Project clean deploy
    if ($LASTEXITCODE -ne 0) {
        throw "Security Platform cannot be published"
    }
} finally {
    $env:NEXUS_USERNAME = $PreviousUsername
    $env:NEXUS_PASSWORD = $PreviousPassword
}

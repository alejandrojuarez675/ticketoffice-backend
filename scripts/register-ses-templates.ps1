param (
    [string]$TemplatesPath = "templates/emails",
    [string]$AwsProfile = "default",
    [string]$AwsRegion = "us-east-1"
)

Write-Host "📨 Registrando templates de SES..."
Write-Host "📂 Carpeta: $TemplatesPath"
Write-Host "🌍 Región: $AwsRegion"
Write-Host "👤 Profile: $AwsProfile"
Write-Host "-------------------------------------"

if (-Not (Test-Path $TemplatesPath)) {
    Write-Error "❌ La carpeta '$TemplatesPath' no existe."
    exit 1
}

Get-ChildItem -Path $TemplatesPath -Filter *.json | ForEach-Object {

    $file = $_.FullName
    Write-Host "➡ Procesando template: $($_.Name)"

    try {
        # Intentar crear el template
        aws ses create-template `
            --cli-input-json file://$file `
            --profile $AwsProfile `
            --region $AwsRegion 2>$null

        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Template creado correctamente"
        } else {
            throw "Template ya existe"
        }
    }
    catch {
        Write-Host "⚠️ Template existente, actualizando..."

        aws ses update-template `
            --cli-input-json file://$file `
            --profile $AwsProfile `
            --region $AwsRegion

        if ($LASTEXITCODE -eq 0) {
            Write-Host "♻️ Template actualizado correctamente"
        } else {
            Write-Error "❌ Error actualizando el template"
            exit 1
        }
    }

    Write-Host "-------------------------------------"
}

Write-Host "🎉 Todos los templates fueron procesados correctamente"

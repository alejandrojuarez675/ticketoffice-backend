# Stop on first error
$ErrorActionPreference = "Stop"

Write-Host "=== Building the application..." -ForegroundColor Cyan
.\gradlew shadowJar

Write-Host "=== Verificando Docker..." -ForegroundColor Cyan
# Verificar si Docker está en ejecución de múltiples maneras
$dockerRunning = $false

# Método 1: Verificar el servicio de Docker
$dockerService = Get-Service -Name "com.docker.service" -ErrorAction SilentlyContinue
if ($dockerService -and $dockerService.Status -eq 'Running') {
    $dockerRunning = $true
}

# Método 2: Verificar si podemos ejecutar un comando de Docker
if (-not $dockerRunning) {
    try {
        $dockerVersion = docker --version 2>&1
        if ($LASTEXITCODE -eq 0) {
            $dockerRunning = $true
        }
    } catch {}
}

# Método 3: Verificar si el proceso de Docker está en ejecución
if (-not $dockerRunning) {
    $dockerProcesses = Get-Process -Name "Docker Desktop" -ErrorAction SilentlyContinue
    if ($dockerProcesses) {
        $dockerRunning = $true
    }
}

if (-not $dockerRunning) {
    Write-Host "Docker no parece estar en ejecución. Iniciando Docker Desktop..." -ForegroundColor Yellow
    
    # Intentar iniciar Docker Desktop
    $dockerPaths = @(
        "${env:ProgramFiles}\Docker\Docker\Docker Desktop.exe",
        "${env:ProgramFiles(x86)}\Docker\Docker\Docker Desktop.exe"
    )
    
    $dockerStarted = $false
    foreach ($dockerPath in $dockerPaths) {
        if (Test-Path $dockerPath) {
            Start-Process -FilePath $dockerPath
            $dockerStarted = $true
            break
        }
    }
    
    if (-not $dockerStarted) {
        Write-Error "No se pudo encontrar Docker Desktop. Por favor, asegúrate de que Docker esté instalado."
        exit 1
    }
    
    # Esperar a que Docker esté listo
    Write-Host "Esperando a que Docker esté listo..." -ForegroundColor Yellow
    $maxAttempts = 30  # 30 intentos * 10 segundos = 5 minutos máximo de espera
    $attempt = 0
    $dockerReady = $false
    
    while ($attempt -lt $maxAttempts -and -not $dockerReady) {
        $attempt++
        try {
            $dockerInfo = docker info 2>&1
            if ($LASTEXITCODE -eq 0) {
                $dockerReady = $true
                Write-Host "Docker está listo!" -ForegroundColor Green
                break
            }
        } catch {}
        
        Write-Host "Esperando a que Docker esté listo... (Intento $attempt/$maxAttempts)" -ForegroundColor Yellow
        Start-Sleep -Seconds 10
    }
    
    if (-not $dockerReady) {
        Write-Warning "No se pudo verificar que Docker esté listo. Continuando de todos modos..."
    }
} else {
    Write-Host "Docker ya está en ejecución." -ForegroundColor Green
}

Write-Host "=== Logging in to Amazon ECR..." -ForegroundColor Cyan
$loginCommand = aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 280874184970.dkr.ecr.us-east-1.amazonaws.com
if ($LASTEXITCODE -ne 0) {
    Write-Error "Failed to log in to Amazon ECR. Please check your AWS credentials and try again."
    exit 1
}

Write-Host "=== Building Docker image..." -ForegroundColor Cyan
docker build -t ticketoffice-backend .
if ($LASTEXITCODE -ne 0) {
    Write-Error "Failed to build Docker image."
    exit 1
}

Write-Host "=== Tagging Docker image..." -ForegroundColor Cyan
docker tag ticketoffice-backend:latest 280874184970.dkr.ecr.us-east-1.amazonaws.com/ticketoffice-backend:latest
if ($LASTEXITCODE -ne 0) {
    Write-Error "Failed to tag Docker image."
    exit 1
}

Write-Host "=== Pushing Docker image to ECR..." -ForegroundColor Cyan
docker push 280874184970.dkr.ecr.us-east-1.amazonaws.com/ticketoffice-backend:latest
if ($LASTEXITCODE -ne 0) {
    Write-Error "Failed to push Docker image to ECR."
    exit 1
}

Write-Host "=== Deployment completed successfully!" -ForegroundColor Green

Add-Type -AssemblyName System.Drawing

$src = "C:\Users\tiwar\OneDrive\Desktop\Brewkery\app\src\main\res\drawable\iconbrew.png"
$resDir = "C:\Users\tiwar\OneDrive\Desktop\Brewkery\app\src\main\res"

# Densities and sizes
# Format: @{ Folder = "mipmap-mdpi"; LegacySize = 48; ForegroundSize = 108 }
$densities = @(
    @{ Folder = "mipmap-mdpi";    LegacySize = 48;  ForegroundSize = 108 },
    @{ Folder = "mipmap-hdpi";    LegacySize = 72;  ForegroundSize = 162 },
    @{ Folder = "mipmap-xhdpi";   LegacySize = 96;  ForegroundSize = 216 },
    @{ Folder = "mipmap-xxhdpi";  LegacySize = 144; ForegroundSize = 324 },
    @{ Folder = "mipmap-xxxhdpi"; LegacySize = 192; ForegroundSize = 432 }
)

function Export-LegacySquare {
    param($srcPath, $dstPath, $size)
    $srcBmp = New-Object System.Drawing.Bitmap($srcPath)
    $dstBmp = New-Object System.Drawing.Bitmap($size, $size, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($dstBmp)

    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $g.CompositingQuality = [System.Drawing.Drawing2D.CompositingQuality]::HighQuality

    $g.Clear([System.Drawing.ColorTranslator]::FromHtml("#3B2019"))
    $g.DrawImage($srcBmp, 0, 0, $size, $size)

    $dstBmp.Save($dstPath, [System.Drawing.Imaging.ImageFormat]::Png)

    $g.Dispose()
    $dstBmp.Dispose()
    $srcBmp.Dispose()
}

function Export-LegacyRound {
    param($srcPath, $dstPath, $size)
    $srcBmp = New-Object System.Drawing.Bitmap($srcPath)
    $dstBmp = New-Object System.Drawing.Bitmap($size, $size, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($dstBmp)

    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $g.CompositingQuality = [System.Drawing.Drawing2D.CompositingQuality]::HighQuality

    $g.Clear([System.Drawing.Color]::Transparent)

    $path = New-Object System.Drawing.Drawing2D.GraphicsPath
    $path.AddEllipse(0, 0, $size, $size)
    $g.SetClip($path)

    $g.Clear([System.Drawing.ColorTranslator]::FromHtml("#3B2019"))
    $g.DrawImage($srcBmp, 0, 0, $size, $size)

    $dstBmp.Save($dstPath, [System.Drawing.Imaging.ImageFormat]::Png)

    $g.Dispose()
    $dstBmp.Dispose()
    $srcBmp.Dispose()
}

function Export-AdaptiveForeground {
    param($srcPath, $dstPath, $canvasSize)
    $srcBmp = New-Object System.Drawing.Bitmap($srcPath)
    $dstBmp = New-Object System.Drawing.Bitmap($canvasSize, $canvasSize, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $g = [System.Drawing.Graphics]::FromImage($dstBmp)

    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::HighQuality
    $g.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::HighQuality
    $g.CompositingQuality = [System.Drawing.Drawing2D.CompositingQuality]::HighQuality

    $g.Clear([System.Drawing.Color]::Transparent)

    # Scale icon artwork so it sits comfortably inside the 72dp safe zone of the 108dp adaptive icon canvas
    $scale = 0.72
    $drawW = [int]($canvasSize * $scale)
    $drawH = [int]($canvasSize * $scale)
    $drawX = [int](($canvasSize - $drawW) / 2)
    $drawY = [int](($canvasSize - $drawH) / 2)

    $g.DrawImage($srcBmp, $drawX, $drawY, $drawW, $drawH)

    $dstBmp.Save($dstPath, [System.Drawing.Imaging.ImageFormat]::Png)

    $g.Dispose()
    $dstBmp.Dispose()
    $srcBmp.Dispose()
}

foreach ($d in $densities) {
    $folderPath = Join-Path $resDir $d.Folder
    if (-not (Test-Path $folderPath)) {
        New-Item -ItemType Directory -Path $folderPath -Force | Out-Null
    }

    # Delete old webp files if they exist
    $oldWebp1 = Join-Path $folderPath "ic_launcher.webp"
    $oldWebp2 = Join-Path $folderPath "ic_launcher_round.webp"
    if (Test-Path $oldWebp1) { Remove-Item $oldWebp1 -Force }
    if (Test-Path $oldWebp2) { Remove-Item $oldWebp2 -Force }

    $legacyPng = Join-Path $folderPath "ic_launcher.png"
    $roundPng  = Join-Path $folderPath "ic_launcher_round.png"
    $forePng   = Join-Path $folderPath "ic_launcher_foreground.png"

    Export-LegacySquare -srcPath $src -dstPath $legacyPng -size $d.LegacySize
    Export-LegacyRound  -srcPath $src -dstPath $roundPng  -size $d.LegacySize
    Export-AdaptiveForeground -srcPath $src -dstPath $forePng -canvasSize $d.ForegroundSize

    Write-Output "Generated icons for $($d.Folder)"
}

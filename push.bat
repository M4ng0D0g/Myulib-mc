@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo 🔍 正在讀取 gradle.properties...

:: 1. 尋找開頭是 mod_version 的那一行，並以等號 (=) 作為分隔符號切割
for /f "tokens=2 delims==" %%I in ('findstr /b "mod_version" gradle.properties') do (
    set RAW_VERSION=%%I
)

:: 2. 去除擷取到的字串中可能包含的空白字元
set VERSION=%RAW_VERSION: =%

:: 3. 檢查是否有成功抓到版本號
if "%VERSION%"=="" (
    echo ❌ 錯誤：無法從 gradle.properties 中找到 mod_version！
    exit /b 1
)

echo 📦 偵測到當前版本為: %VERSION%
echo 🚀 開始自動發布流程...

:: 4. 將所有變更加入追蹤
git add .

:: 5. 提交 Commit，訊息自動帶入版本號
git commit -m "Release %VERSION%"

:: 6. 建立 Git 標籤 (JitPack 抓取版本所需)
git tag %VERSION%

:: 7. 推送程式碼與標籤到 GitHub
:: 注意：如果您的主分支名稱是 master 而不是 main，請將下方的 main 改為 master
git push origin main
git push origin %VERSION%

echo.
echo ✅ 成功推播並建立標籤: %VERSION%
echo 雲端打包服務 (JitPack) 現在應該已經開始建置這個新版本了！
endlocal
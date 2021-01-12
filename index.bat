@echo off
cls
if exist build rmdir /s /q build
mkdir build
cd src
javac  -cp com/krzem/stickman_game/modules/jogl-all-natives-windows-amd64.jar;com/krzem/stickman_game/modules/jogl-all.jar;com/krzem/stickman_game/modules/gluegen-rt-natives-windows-amd64.jar;com/krzem/stickman_game/modules/gluegen-rt.jar;com/krzem/stickman_game/modules/jogl-all-natives-windows-amd64.jar;com/krzem/stickman_game/modules/jogl-all.jar;com/krzem/stickman_game/modules/gluegen-rt-natives-windows-amd64.jar;com/krzem/stickman_game/modules/gluegen-rt.jar;com/krzem/stickman_game/modules/jogl-all-natives-windows-amd64.jar;com/krzem/stickman_game/modules/jogl-all.jar;com/krzem/stickman_game/modules/gluegen-rt-natives-windows-amd64.jar;com/krzem/stickman_game/modules/gluegen-rt.jar; -d ../build com/krzem/stickman_game/Main.java&&jar cvmf ../manifest.mf ../build/stickman_game.jar -C ../build *&&goto run
cd ..
goto end
:run
cd ..
pushd "build"
for /D %%D in ("*") do (
	rd /S /Q "%%~D"
)
for %%F in ("*") do (
	if /I not "%%~nxF"=="stickman_game.jar" del "%%~F"
)
popd
cls
java -jar build/stickman_game.jar
:end

jarsigner -keystore ./SosiaPos.dat -storepass sosiapos ./dist/SOSIA_POS_main_for_hair.jar SosiaRoot

REM jarsigner -keystore ./SosiaPos.dat -storepass sosiapos ./dist/lib/*.jar SosiaRoot
for %%f in (./dist/lib/*.jar) do jarsigner -keystore ./SosiaPos.dat -storepass sosiapos ./dist/lib/%%f SosiaRoot
pause

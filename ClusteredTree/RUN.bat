@echo off
:: tenfile seed numFitness sizePopulation pLocalSearch
setlocal abc
goto :main
:main 
setlocal
	for /f %%j in (Type_1_Small.txt) do (
		for /l %%i in (1,1,30) do (
			java -cp build/classes Main %%j %%i 50000 100 0 Type_1_Small
		)
	)
	for /f %%j in (Type_5_Small.txt) do (
		for /l %%i in (1,1,30) do (
			java -cp build/classes Main %%j %%i 50000 100 0 Type_5_Small
		)
	)
	for /f %%j in (Type_6_Small.txt) do (
		for /l %%i in (1,1,30) do (
			java -cp build/classes Main %%j %%i 50000 100 0 Type_6_Small
		)
	)
	for /f %%j in (Type_1_Small.txt) do (
		for /l %%i in (1,1,30) do (
			java -cp build/classes Main %%j %%i 50000 100 0.05 Type_1_Small
		)
	)
	for /f %%j in (Type_5_Small.txt) do (
		for /l %%i in (1,1,30) do (
			java -cp build/classes Main %%j %%i 50000 100 0.05 Type_5_Small
		)
	)
	for /f %%j in (Type_6_Small.txt) do (
		for /l %%i in (1,1,30) do (
			java -cp build/classes Main %%j %%i 50000 100 0.05 Type_6_Small
		)
	)
endlocal
goto :eof
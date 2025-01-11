			@ECHO OFF
			SET BINDIR=%~dp0
			CD /D "%BINDIR%"
			:start
			start java GameServer.StartGameserver
			goto start
#SingleInstance, force

Gui, Add, Text, x10 y20 w200 h13, Select Path:
Gui, Add, DropDownList, vSelectedPath x10 y50 w400

Loop, Files, z:\usr\glibc\*, D
{
    If (A_LoopFileName ~= "^wine")
    {
        GuiControl,, SelectedPath, mobox: %A_LoopFileName%      path: z:\usr\glibc\%A_LoopFileName%
    }
}

Loop, Files, z:\usr\glibc\opt\wine\*, D
{
    GuiControl,, SelectedPath, DarkOS: %A_LoopFileName%      path: z:\usr\glibc\opt\wine\%A_LoopFileName%
}



Gui, Add, Button, x10 y80 w400 h23 gInstallButton, Install winefiles
Gui, Add, Button, x10 y110 w400 h23 gUnInstallButton, Remove winefiles
Gui, Show, w420 h150, Ugly winefiles install/remove tool
return

InstallButton:
Gui, Submit, NoHide
GuiControlGet, SelectedPath


if InStr(SelectedPath, "DarkOS")
{
     path32_add := "\wine\lib\wine\i386-windows"
     path64_add := "\wine\lib\wine\x86_64-windows"
}

if InStr(SelectedPath, "mobox:")
{
     path32_add := "\lib\wine\i386-windows"
     path64_add := "\lib\wine\x86_64-windows"
}

SelectedPath := StrSplit(SelectedPath, "path: ")[2]

MsgBox, 4,, Copy Winefiles to %SelectedPath%?
IfMsgBox, Yes
{
    
   	path32 :=  SelectedPath
	path32 .= path32_add
	CopyFolder("32\", path32 )

	path64 :=  SelectedPath
	path64 .=  path64_add
	CopyFolder("64\", path64)



    MsgBox, Done.
}

return



UnInstallButton:
Gui, Submit, NoHide
GuiControlGet, SelectedPath

if InStr(SelectedPath, "DarkOS")
{
     path32_add := "\wine\lib\wine\i386-windows"
     path64_add := "\wine\lib\wine\x86_64-windows"
}

if InStr(SelectedPath, "mobox:")
{
     path32_add := "\lib\wine\i386-windows"
     path64_add := "\lib\wine\x86_64-windows"
}


SelectedPath := StrSplit(SelectedPath, "path: ")[2]

MsgBox, 4,, Remove modified Winefiles from %SelectedPath%?

IfMsgBox, Yes
{
    
   	path32 :=  SelectedPath
	path32 .= path32_add
	CopyFolder("unmodified\32\", path32)

	path64 :=  SelectedPath
	path64 .=  path64_add
	CopyFolder("unmodified\64\", path64)



    MsgBox, Done.
}

return



CopyFolder(from, to)
{
    Loop, %from%\*
    {
        FileCopy, %A_LoopFileFullPath%, %to%\%A_LoopFileName%, 1
    }
}



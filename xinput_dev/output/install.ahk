#SingleInstance, force

Gui, Add, Text, x10 y20 w200 h13, Select Path:
Gui, Add, DropDownList, vSelectedPath x10 y50 w380

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



Gui, Add, Button, x12 y79 w120 h80 gInstallButton_L, Install Latest
Gui, Add, Button, x12 y169 w380 h40 gUnInstallButton, Remove winefiles
Gui, Add, Button, x142 y79 w120 h80 gInstallButton_H , Install Hybrid
Gui, Add, Button, x272 y79 w120 h80 gInstallButton_O , Install Old
Gui, Add, Text, x10 y20 w200 h13 , Select Path:

Gui, Show, h220 w400, XinputBridge wine tool
return

InstallButton_L:
Gui, Submit, NoHide
GuiControlGet, SelectedPath
Copy(SelectedPath, "files\latest", "Install Latest Xinput Files to")
return

InstallButton_H:
Gui, Submit, NoHide
GuiControlGet, SelectedPath
Copy(SelectedPath, "files\hybrid", "Install Hybrid Xinput Files to")
return

InstallButton_O:
Gui, Submit, NoHide
GuiControlGet, SelectedPath
Copy(SelectedPath, "files\old", "Install Old Xinput Files to")
return

UnInstallButton:
Gui, Submit, NoHide
GuiControlGet, SelectedPath
Copy(SelectedPath, "files\unmodified", "Remove Xinput Files from")
return



CopyFolder(from, to)
{

    Loop, %from%\*
    {
        FileCopy, %A_LoopFileFullPath%, %to%\%A_LoopFileName%, 1
    }
}

Copy(dest, source, msg) {
    SelectedPath := dest
if(StrLen(SelectedPath)<10){
msgbox, First Select Path
return
}


    if InStr(SelectedPath, "DarkOS") {
        path32_add := "\wine\lib\wine\i386-windows"
        path64_add := "\wine\lib\wine\x86_64-windows"
    }

    if InStr(SelectedPath, "mobox:") {
        path32_add := "\lib\wine\i386-windows"
        path64_add := "\lib\wine\x86_64-windows"
    }

    SelectedPath := StrSplit(SelectedPath, "path: ")[2]

    MsgBox, 4,, %msg% %SelectedPath%?

    IfMsgBox, Yes 
{
        path32 :=  SelectedPath
        path32 .= path32_add
        CopyFolder(source "\32\", path32)

        path64 :=  SelectedPath
        path64 .=  path64_add
        CopyFolder(source "\64\", path64)

        MsgBox, Done.
    }
}



return

GuiClose:
GuiEscape:
ExitApp





# Xinput Support for Mobox and DarkOS

This repository provides Xinput support for Mobox and DarkOS. Please note that this is the initial test version, so not everything may work perfectly. I will continue to work on improving it and welcome any suggestions for enhancement.

## Acknowledgements

I want to express my sincere gratitude to everyone who encouraged me to implement this feature. This is just the beginning, as ynixt rightly said: "I know.. is ugly, but this can be the start of a great idea :)".

Special thanks to ynixt for the initial idea. You can find more details about the inception of this project in the following link: [ynixt's comment](https://github.com/olegos2/mobox/issues/125#issuecomment-1987031399).

I would also like to extend my thanks to brunodev85 for providing the Wine libraries from Winlator. You can explore the Wine patches at [brunodev85/winlator](https://github.com/brunodev85/winlator/tree/main/wine_patches/dlls).

Additionally, I utilized AndroidIDE for building most of the components. You can check out AndroidIDE at [AndroidIDEOfficial/AndroidIDE](https://github.com/AndroidIDEOfficial/AndroidIDE).

# Installation Instructions

Download winefiles_with_install.zip and run install_winefiles.exe

the source of the [installer](https://github.com/Ilan12346-maya/XinputBridge/blob/main/xinput_dev/output/install.ahk). 

Generaly it's recommended to utilize a separate container. 

wine 9.3 should be the most stable atm.

## Installation Manual


### Mobox:

Copy 32-bit files to: `usr/glibc/<wine_version>/lib/wine/i386-windows`

Copy 64-bit files to: `usr/glibc/<wine_version>/lib/wine/x86_64-windows`

*Note: Replace `<wine_version>` with the appropriate wine version. Currently, it might be "wine-9.3-vanilla-wow64".

You can also install all directly by termux:                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
```console
cp -r /storage/emulated/0/Download/winefiles/winefiles/32/* $PREFIX/glibc/<wine_version>/lib/wine/i386-windows
cp -r /storage/emulated/0/Download/winefiles/winefiles/64/* $PREFIX/glibc/<wine_version>/lib/wine/x86_64-windows
```

set all xinput dlls to Buildin than Nativ on winecfg -> libarys


### DarkOS:

Copy 32-bit files to: `usr/glibc/opt/wine/<container_version>/wine/lib/wine_i386-windows`

Copy 64-bit files to: `usr/glibc/opt/wine/<container_version>/wine/lib/wine/x86_64-windows`

*Note: Replace `<container_version>` with the appropriate container version.

## Compatibility

This implementation has been tested on Redmagic 8S Pro with Android 13 with Xbox One and Gamesir X2 gamepad. Bluetooth connectivity should work, although it hasn't been tested yet.
Any gamepad recognized by Android should work.

## Compile winefiles your self

evrything you will need is in "xinput_dev" 

Also main.c, modified version of brunos files [brunodev85/winlator](https://github.com/brunodev85/winlator/tree/main/wine_patches/dlls/xinput)


## Frequently Asked Questions

### With this is possible to play with gamepad any game?

The game needs to be compatible with XInput. Generally speaking, all modern games are compatible, but older games, like NFSU2, need a patch for that.

### Can I use L2/R2 as triggers instead of buttons?

For now this isn't supported as can be readed on [#2](https://github.com/Ilan12346-maya/XinputBridge/issues/2).

## Contribution

Contributions to this project are welcome! Feel free to report any issues or suggest improvements by opening an issue or a pull request.

## Support

Found any problem? Join our [Telegram](https://t.me/+YLyovfrXSeYwMmUy) or just open a [new issue](https://github.com/Ilan12346-maya/XinputBridge/issues/new).

## License

This project is licensed under the GNU General Public License Version 3.0 (GPL-3.0). Everyone is entitled to improve the project and use it for meaningful purposes as long as the terms of the GPL-3.0 license are complied with. For more information, see the [LICENSE](LICENSE) file.

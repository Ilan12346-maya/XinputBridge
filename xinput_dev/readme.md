### Easy and Fast Xinput Compilation - YEAH!

#### Use of modified Scripts from Kron4ek

- [Wine-Builds Repository by Kron4ek](https://github.com/Kron4ek/Wine-Builds.git)

#### Use of modified main.c from brunodev85

- [Winlator Repository by brunodev85](https://github.com/brunodev85/winlator.git)

#### Use of Wine from wine-mirror

- [Wine-mirror Repository](https://github.com/wine-mirror/wine.git)

1. Change the path in `prepare.sh` and `compile_xinput.sh` to the correct working directory.

2. Navigate to the `work_temp` directory and download Wine by running:

    ```bash
    git clone --branch wine-9.3 https://github.com/wine-mirror/wine.git
    ```
    
    Replace `wine-9.3` with the version you prefer.
    
    There have to be a wine folder inside the work_temp folder now.

   - If you haven't already, execute `create_ubuntu_bootstraps.sh` once.
   
   - Run `prepare.sh` once but whenever there's a new Wine version, rerun `prepare.sh`.
  
   - Maybe you will need 
   
    ```bash
    sudo apt install autoconf bubblewrap
    ```

   Ensure your `main.c` file is located in the input folder.

3. Run `compile_xinput.sh`.

    - The initial compilation may take 1 minutes depending on your machine.
    - Subsequent compilations should only take around 5 seconds.

The Xinput DLLs are now available in the output folder, both 32-bit and 64-bit. You can copy them to Mobox/DarkOS.




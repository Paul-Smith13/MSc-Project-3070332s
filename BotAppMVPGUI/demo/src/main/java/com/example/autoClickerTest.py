import pyautogui
import time
import keyboard
import threading
import os
import sys
import signal
import psutil

print(pyautogui.size())

# Set PyAutoGUI failsafe (move mouse to upper-left corner to stop)
pyautogui.FAILSAFE = True

delay = 1
start_stop_key = 's'
exit_key = 'q'

clicking = False
continue_running = True

DEBOUNCE_INTERVAL = 0.5

#When running a problem arose: pressing q only stopped current instance of class
#Needed way to stop all methods running, hence:
LOCK_FILE = "autoclicker.lock"
def check_lock_file():
    if os.path.exists(LOCK_FILE):
        script_name = os.path.basename(sys.argv[0])
        for process in psutil.process_iter(['name', 'cmdline']):
            try:
                if process.info['name'] == 'python.exe' and script_name in ' '.join(process.info['cmdline']):
                    print("Already running. Exiting.")
                    sys.exit(1)
            except (psutil.NoSuchProcess, psutil.AccessDenied):
                pass
        print("Removing stale lock file.")
        os.remove(LOCK_FILE)
    #This creates the lock file.
    open(LOCK_FILE, 'w').close()

def remove_lock_file():
    if os.path.exists(LOCK_FILE):
        os.remove(LOCK_FILE)

class AutoClicker(threading.Thread):
    def __init__(self, delay, button):
        super().__init__()
        self.delay = delay
        self.button = button
        self._last_s_press_time = 0.0
        self._last_q_press_time = 0.0

    def run(self):
        global clicking, continue_running
        print("Autoclicker started. Press 's' to start/stop, 'q' to exit")
        time.sleep(2)

        while continue_running:
            current_time = time.time()

            #Hotkey Detection
            if keyboard.is_pressed(start_stop_key):
                if (current_time - self._last_s_press_time) > DEBOUNCE_INTERVAL:
                    clicking = not clicking
                    if clicking:
                        print("Auto-clicker started.", flush=True)
                    else:
                        print("Auto-clicker stopped.", flush=True)
                    self._last_s_press_time = current_time
                time.sleep(1)
            elif keyboard.is_pressed(exit_key):
                if (current_time - self._last_q_press_time) > DEBOUNCE_INTERVAL:
                    print("'Q' pressed, exiting.")
                    clicking = False
                    continue_running = False
                    self._last_q_press_time = current_time
                    break
                time.sleep(1)
            #Clicking logic
            if clicking:
                print(f"Clicked at {pyautogui.position()}")
                pyautogui.click()
                time.sleep(self.delay)
            else:
                time.sleep(1)

    def autoclicker():
        print("Autoclicker will start in 5 seconds. Move mouse to desired position.")
        time.sleep(5)  # Give 5 seconds to prepare
        print("Started clicking. Press 'q' to stop.")
        
        running = True
        while running:
            try:
                pyautogui.click(x = 100, y = 200, duration=1)
                time.sleep(2)
                if keyboard.is_pressed('q'):
                    running = False
            except Exception as e:
                print(f"Error during click: {e}")
                break
        print("Autoclicker stopped.")

def listen_for_exit():
    global clicking, continue_running
    keyboard.wait('q')  # Wait for 'q' to be pressed
    clicking = False
    continue_running = False
    print("Exit key detected.")
    remove_lock_file()
    os.kill(os.getpid(), signal.SIGTERM)

if __name__ == "__main__":
    check_lock_file()
    try:
        # Run autoclicker in a separate thread to avoid blocking
        click_thread = AutoClicker(delay, button="left") #Instantiate an autoclicker object
        click_thread.start()
        # Listen for 'q' in the main thread
        listen_for_exit()
        click_thread.join()  # Wait for the clicker thread to finish
    finally:
        remove_lock_file()
import pyautogui
import time
import keyboard
import threading
import os
import sys
import signal
import psutil

#Script to test attacking rats

class clicker(threading.Thread):
    def __init__(self, delay, button, target_image_path):
        super().__init__()
        self.delay = delay
        self.button = button
        self.running = False #This will be used to control the thread's execution
        self.current_action = None #Store what thread is currently doing
        self.target_image_path = target_image_path

    def _click_at_location(self, location):
        if location:
            center_x, center_y = pyautogui.center(location)
            pyautogui.click(x = center_x, y = center_y, button=self.button)
            print(f"Clicked location {center_x, center_y}")
            time.sleep(self.delay)
            return True
        return False
    
    def click_image(self, image_path, confidence = None):
        try:
            if confidence is not None:
                location = pyautogui.locateOnScreen(image_path, confidence=confidence)
            else:
                location = pyautogui.locateOnScreen(image_path)
            if self._click_at_location(location):
                return True
            else:
                print(f"{image_path} wasn't found on screen")
                return False            
        except pyautogui.PyAutoGUIException as e:
            print(f"{e}")
        except FileNotFoundError:
            print(f"{image_path} not found")
        except Exception as e:
            print(f"{e}")
            return False    

    def click_multiple_images(self, image_paths, confidence = None):
        results = []
        self.current_action = f"{[os.path.basename(p) for p in image_paths]}"
        for image_path in image_paths:
            found_and_clicked = self.click_image(image_path, confidence = confidence)
            results.append(found_and_clicked)
            if found_and_clicked:
                time.sleep(self.delay * 2)
        return results
    
    def click_all_instances_of_images(self, image_path, confidence = None):
        clicked_count = 0
        self.current_action = f"{os.path.basename(image_path)}"
        try:
            if confidence is not None:
                locations = list(pyautogui.locateAllOnScreen(image_path, confidence=confidence))
            else:
                locations = list(pyautogui.locateAllOnScreen(image_path))

            if not locations:
                return 0

            for location in locations:
                if self._click_at_location(location):
                    clicked_count +=1
                time.sleep(self.delay)
            return clicked_count
        except pyautogui.PyAutoGUIException as e:
            print(f"{os.path.basename(image_path)} : {e}")
            return 0
        except FileNotFoundError as e:
            print(f"{os.path.basename(image_path)} : {e}")
            return 0
        except Exception as e:
            print(f"{os.path.basename(image_path)} : {e}")
            return 0
        
    def run(self):
        self.running = True
        if not self.target_image_path or not os.path.exists(self.target_image_path):
            print(f"ERROR: didn't find image path {self.target_image_path}")
            self.stop()

        while self.running:
            self.current_action = f"Searching for {os.path.basename(self.target_image_path)}"
            rat_found = self.click_image(self.target_image_path, confidence=0.6)
            if rat_found:
                print("Found a rat.")
                time.sleep(self.delay * 5)
            else:
                print("Rat not found, searching again.")
                time.sleep(self.delay * 0.5)
        print("No longer searching for rat. Stopped.")

    def stop(self):
        self.running = False
        print("Clicker thread stopped.")

if __name__ == "__main__":
    print("Starting Rat attacker...")
    time.sleep(5)
    print("Started")


    script_current_dir = os.path.dirname(os.path.abspath(__file__))
    root_project_dir = os.path.abspath(os.path.join(script_current_dir, "..", "..", "..", "..", ".."))
    monster_file_name = 'rat.png'
    monster_file_path = os.path.join(root_project_dir, monster_file_name)
    
    if not os.path.exists(monster_file_path):
        print(f"ERROR: didn't find {monster_file_path}. Terminating.")
        sys.exit(1)
    
    a_clicker = clicker(delay = 0.5, button='left', target_image_path=monster_file_path)
    a_clicker.start()

    try: 
        while a_clicker.is_alive():
            if keyboard.is_pressed('q'):
                print("'q' was pressed. Initialising termination.")
                a_clicker.stop()
                break
            time.sleep(1)
    except KeyboardInterrupt:
        print("Ctrl + C used to keyboard interrupt script.")
        a_clicker.stop()
    finally:
        if a_clicker.is_alive():
            print("Waiting for clicker to finish...")
            a_clicker.join(timeout=5)
            if a_clicker.is_alive():
                print("CAUTION: clicker thread didn't terminate as expected.\nCAUTION: clicker could still be running in background.")
        print("Main script finished")


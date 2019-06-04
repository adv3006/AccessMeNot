import pyrebase
import time


config = {
    "apiKey": "", 
    "authDomain": "",
    "databaseURL": "", 
    "projectId": "",
    "storageBucket": "",
    "messagingSenderId": ""
}

firebase = pyrebase.initialize_app(config)

storage = firebase.storage()

try: 
    while True:
        print("Updating file to firebase...")
        storage.child("ap_log.txt").put("ap_log.txt")
        time.sleep(30)
except KeyboardInterrupt:
    print("Finish...")

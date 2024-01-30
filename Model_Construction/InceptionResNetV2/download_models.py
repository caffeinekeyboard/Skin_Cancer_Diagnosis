#RUN THIS FILE TO DOWNLOAD THE INCEPTION-RESNETV2 MODEL.
'''
    ----------24 January 2024 | Aarya Bhave | Commited to Skin_Cancer_Diagnosis----------
    I have unfortunately ran out of my monthly quota for git lfs.
    So here's a python script to download the model.
    THIS FILE GETS SAVED IN YOUR CWD. PLEASE CHECK YOUR CWD AND CHECK ENVIRONMENT BEFORE RUNNING!!!
    Godspeed. :-)    
'''
import requests
import os

def download_file_from_google_drive(id, destination):
    URL = "https://docs.google.com/uc?export=download"

    session = requests.Session()

    response = session.get(URL, params = { 'id' : id }, stream = True)
    token = get_confirm_token(response)

    if token:
        params = { 'id' : id, 'confirm' : token }
        response = session.get(URL, params = params, stream = True)

    save_response_content(response, destination)    

def get_confirm_token(response):
    for key, value in response.cookies.items():
        if key.startswith('download_warning'):
            return value

    return None

def save_response_content(response, destination):
    CHUNK_SIZE = 32768

    with open(destination, "wb") as f:
        for chunk in response.iter_content(CHUNK_SIZE):
            if chunk: # filter out keep-alive new chunks
                f.write(chunk)

os.mkdir('Models')
file_id_1 = '1-7cwaHnlEwLELT5QbEkirtFCjseLNo0d'
file_id_2 = '1irtCf1bJSo65yb8sZ72zT_pJOenxWaSP'
file_id_3 = '1ssx7T7bUMsUC-FE53ZRX_Zwj_bxgNN3Z'
file_id_4 = '1Z2YMA-UTOYWN75pKUxJI1upWwqMIfmMK'
destination_1 = os.getcwd() + "/Models/InceptionResNetV2_HDF5.hdf5"
destination_2 = os.getcwd() + "/Models/InceptionResNetV2_H5.h5"
destination_3 = os.getcwd() + "/Models/InceptionResNetV2_Lite.tflite"
destination_4 = os.getcwd() + "/Models/InceptionResNetV2_Lite_Optimized.tflite"
download_file_from_google_drive(file_id_1, destination_1)
download_file_from_google_drive(file_id_2, destination_2)
download_file_from_google_drive(file_id_3, destination_3)
download_file_from_google_drive(file_id_4, destination_4)

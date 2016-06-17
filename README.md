# Speech Expression

Text-to-Speech handler for Lumen Robot Friend with expressive intonation support.

## Cara Instalasi MBROLA + Voices + eSpeak di Windows

1. Buat folder `C:\mbroladb`
2. Download [MBROLA for PC/DOS](http://tcts.fpms.ac.be/synthesis/mbrola/bin/pcdos/mbr301d.zip), dan extract ke `C:\mbrola`. Sumber: http://tcts.fpms.ac.be/synthesis/mbrola.html
3. Install [eSpeak](http://espeak.sourceforge.net/).
    **Penting:** pada waktu install eSpeak tulis `mb-id1` pada salah satu voice name yang kosong.
4. Install [MbrolaTools35.exe](http://www.tcts.fpms.ac.be/synthesis/mbrola/bin/pcwin/MbrolaTools35.exe). eSpeak for Windows membutuhkannya untuk dapat memanggil MBROLA.
5. Download **id1: Indonesian Male (4Mb) Arry Arman** dari http://tcts.fpms.ac.be/synthesis/mbrola/mbrcopybin.html,
    extract lalu copy folder `id1-001010`  ke dalam folder `C:\mbroladb`
6. Copy file `id1` (**file** di dalam folder `id1-001010`, bukan foldernya itu sendiri) ke dalam `C:\Program Files (x86)\eSpeak\espeak-data\mbrola`
7. Download [ffmpeg for Windows](https://ffmpeg.zeranoe.com/builds/), extract ke `D:\` lalu rename foldernya menjadi
    `ffmpeg`, di dalam folder `ffmpeg` sudah harus ada hasil ekstrak(`D:\ffmpeg`).
8. Tambahkan MBROLA, eSpeak, dan ffmpeg ke environment `PATH`, contoh:
    `C:\mbrola;C:\Program Files (x86)\eSpeak\command_line;D:\ffmpeg\bin`
9. Test: Jalankan Command Prompt (harus dibuka baru, tidak boleh yang sudah jalan), lalu pastikan semua perintah berikut menghasilkan output (bukan "not recognized as command"):

        mbrola
        espeak "Hello world"
        espeak -v mb-id1 "Apa kabar?"
    
    Bila muncul `Can't load mbrola.dll. mbrola voice not found`, artinya Mbrola Tools belum diinstall.

## Cara Instalasi MBROLA + Voices + eSpeak di Linux Mint / Ubuntu

1. Install MBROLA, voice English, voice bahasa Indonesia, eSpeak, dan `libavcodec54`, caranya:

        sudo apt-get install mbrola mbrola-us1 mbrola-id1 espeak libavcodec54

2. Test: Jalankan Terminal/Konsole, lalu pastikan semua perintah berikut menghasilkan output (bukan "not recognized as command"):

        mbrola
        espeak "Hello world"
        espeak -v mb-id1 "Apa kabar?"

## Menjalankan Lumen Speech Synthesis dari Source

1. Pastikan MBROLA, MBROLA voice Indonesia, dan eSpeak sudah terinstall dan berfungsi baik
2. Clone project lumen-sdk:

        sudo apt-get install mbrola mbrola-us1 mbrola-id1

3. Clone project speech-synthesis
4. Buka project `speech-synthesis` di IntelliJ IDEA sebagai Maven project
5. New Module from Existing Sources > buka folder `speech-synthesis`
6. Klik `speech-synthesis`, pilih src>main>java, klik kanan di `SpeechSynthesisApp`, pilih create 'speech synthesis'
pada bagian working directory isi `$MODULE_DIR$`, ceklis "Single Instance Only"
7. Klik `speech-synthesis`, pilih src>main>java, klik kanan di `SpeechSynthesisApp` pilih run    

## Arabic

Not working yet (eSpeak version mismatch with arabic data version) :(

    2015-10-07 19:48:08.987  INFO 6072 --- [abbitMQConsumer] o.l.l.s.e.SpeechExpressionRouter         : Got speech lang-legacy=ar: CommunicateAction{inLanguage=ar-SA, object='السَّلاَمُ عَلَيْكُمْ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ', actionStatus=null, avatarId='nao1', emotionKind=null}
    2015-10-07 19:48:09.163  INFO 6072 --- [abbitMQConsumer] o.l.l.s.e.SpeechExpressionRouter         : [espeak, -s, 130, -v, mb-ar1, -w, C:\Users\ceefour\AppData\Local\Temp\lumen-speech-expression_2395788186719845122.wav, "السَّلاَمُ عَلَيْكُمْ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ"]: Wrong version of espeak-data 0x14709 (expects 0x14801) at C:\Program Files (x86)\eSpeak\espeak-data

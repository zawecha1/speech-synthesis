# Speech Expression

Text-to-Speech handler for Lumen Robot Friend with expressive intonation support.

## MBROLA dan eSpeak

## Langkah2 Install TTS Salita

1. Buat folder `C:\mbroladb`
2. Download [MBROLA for PC/DOS](http://tcts.fpms.ac.be/synthesis/mbrola.html), dan extract ke `C:\mbrola`.
3. Install [eSpeak](http://espeak.sourceforge.net/).
    **Penting:** pada waktu install eSpeak tulis `mb-id1` pada salah satu voice name yang kosong.
4. Download **id1: Indonesian Male (4Mb) Arry Arman** dari http://tcts.fpms.ac.be/synthesis/mbrola/mbrcopybin.html,
    extract lalu copy folder `id1-001010`  ke dalam folder `C:\mbroladb`
5. Copy file `id1` (dalam folder `id1-001010`) ke dalam `C:\Program Files (x86)\eSpeak\espeak-data\mbrola`
6. Download [ffmpeg for Windows](https://ffmpeg.zeranoe.com/builds/), extract ke `D:\` lalu rename foldernya menjadi
    `ffmpeg`, di dalam folder `ffmpeg` sudah harus ada hasil ekstrak(`D:\ffmpeg`).
7. Tambahkan MBROLA, eSpeak, dan ffmpeg ke environment `PATH`, contoh:
    `C:\mbrola;C:\Program Files (x86)\eSpeak\command_line;D:\ffmpeg\bin`
8. Test: Jalankan Command Prompt (harus dibuka baru, tidak boleh yang sudah jalan), lalu pastikan perintah berikut menghasilkan output (bukan "not recognized as command"):

        mbrola
        espeak "Hello world"
        espeak -v mb-id1 "Apa kabar?"
    
9. Klik `speech-synthesis`, pilih src>main>java, klik kanan di `SpeechSynthesisApp`, pilih create'specch synthesis'
pada bagian working directory isi `$MODULE_DIR$`, ceklis "Single Instance Only"
10.  Klik `speech-synthesis`, pilih src>main>java, klik kanan di `SpeechSynthesisApp` pilih run    


## Arabic

Not working yet (eSpeak version mismatch with arabic data version) :(

    2015-10-07 19:48:08.987  INFO 6072 --- [abbitMQConsumer] o.l.l.s.e.SpeechExpressionRouter         : Got speech lang-legacy=ar: CommunicateAction{inLanguage=ar-SA, object='السَّلاَمُ عَلَيْكُمْ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ', actionStatus=null, avatarId='nao1', emotionKind=null}
    2015-10-07 19:48:09.163  INFO 6072 --- [abbitMQConsumer] o.l.l.s.e.SpeechExpressionRouter         : [espeak, -s, 130, -v, mb-ar1, -w, C:\Users\ceefour\AppData\Local\Temp\lumen-speech-expression_2395788186719845122.wav, "السَّلاَمُ عَلَيْكُمْ وَرَحْمَةُ اللهِ وَبَرَكَاتُهُ"]: Wrong version of espeak-data 0x14709 (expects 0x14801) at C:\Program Files (x86)\eSpeak\espeak-data

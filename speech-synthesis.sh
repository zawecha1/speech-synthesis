#!/bin/bash
SCRIPT_DIR="$(dirname $0)"
java -Xms256m -Xmx256m -Djava.awt.headless=true -cp $SCRIPT_DIR'/target/dependency/*':$SCRIPT_DIR'/target/classes' org.lskk.lumen.speech.synthesis.SpeechSynthesisApp "$@"

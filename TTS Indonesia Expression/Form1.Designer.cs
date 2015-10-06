namespace TTS_Indonesia_Expression
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.rtbTextInput = new System.Windows.Forms.RichTextBox();
            this.buttonGenerate = new System.Windows.Forms.Button();
            this.radioHappy = new System.Windows.Forms.RadioButton();
            this.radioAngry = new System.Windows.Forms.RadioButton();
            this.radioSad = new System.Windows.Forms.RadioButton();
            this.groupEmotion = new System.Windows.Forms.GroupBox();
            this.radioNormal = new System.Windows.Forms.RadioButton();
            this.buttonSetting = new System.Windows.Forms.Button();
            this.groupEmotion.SuspendLayout();
            this.SuspendLayout();
            // 
            // rtbTextInput
            // 
            this.rtbTextInput.Location = new System.Drawing.Point(12, 12);
            this.rtbTextInput.Name = "rtbTextInput";
            this.rtbTextInput.Size = new System.Drawing.Size(339, 96);
            this.rtbTextInput.TabIndex = 0;
            this.rtbTextInput.Text = "";
            this.rtbTextInput.KeyDown += new System.Windows.Forms.KeyEventHandler(this.rtbInput_KeyDown);
            // 
            // buttonGenerate
            // 
            this.buttonGenerate.Location = new System.Drawing.Point(274, 115);
            this.buttonGenerate.Name = "buttonGenerate";
            this.buttonGenerate.Size = new System.Drawing.Size(75, 23);
            this.buttonGenerate.TabIndex = 7;
            this.buttonGenerate.Text = "Generate";
            this.buttonGenerate.UseVisualStyleBackColor = true;
            this.buttonGenerate.Click += new System.EventHandler(this.buttonGenerate_Click);
            // 
            // radioHappy
            // 
            this.radioHappy.AutoSize = true;
            this.radioHappy.Location = new System.Drawing.Point(6, 47);
            this.radioHappy.Name = "radioHappy";
            this.radioHappy.Size = new System.Drawing.Size(56, 17);
            this.radioHappy.TabIndex = 10;
            this.radioHappy.TabStop = true;
            this.radioHappy.Text = "Happy";
            this.radioHappy.UseVisualStyleBackColor = true;
            // 
            // radioAngry
            // 
            this.radioAngry.AutoSize = true;
            this.radioAngry.Location = new System.Drawing.Point(6, 71);
            this.radioAngry.Name = "radioAngry";
            this.radioAngry.Size = new System.Drawing.Size(52, 17);
            this.radioAngry.TabIndex = 11;
            this.radioAngry.TabStop = true;
            this.radioAngry.Text = "Angry";
            this.radioAngry.UseVisualStyleBackColor = true;
            // 
            // radioSad
            // 
            this.radioSad.AutoSize = true;
            this.radioSad.Location = new System.Drawing.Point(6, 95);
            this.radioSad.Name = "radioSad";
            this.radioSad.Size = new System.Drawing.Size(44, 17);
            this.radioSad.TabIndex = 12;
            this.radioSad.TabStop = true;
            this.radioSad.Text = "Sad";
            this.radioSad.UseVisualStyleBackColor = true;
            // 
            // groupEmotion
            // 
            this.groupEmotion.Controls.Add(this.radioNormal);
            this.groupEmotion.Controls.Add(this.radioHappy);
            this.groupEmotion.Controls.Add(this.radioSad);
            this.groupEmotion.Controls.Add(this.radioAngry);
            this.groupEmotion.Location = new System.Drawing.Point(12, 114);
            this.groupEmotion.Name = "groupEmotion";
            this.groupEmotion.Size = new System.Drawing.Size(86, 131);
            this.groupEmotion.TabIndex = 13;
            this.groupEmotion.TabStop = false;
            this.groupEmotion.Text = "Emotion";
            // 
            // radioNormal
            // 
            this.radioNormal.AutoSize = true;
            this.radioNormal.Location = new System.Drawing.Point(6, 24);
            this.radioNormal.Name = "radioNormal";
            this.radioNormal.Size = new System.Drawing.Size(58, 17);
            this.radioNormal.TabIndex = 13;
            this.radioNormal.TabStop = true;
            this.radioNormal.Text = "Normal";
            this.radioNormal.UseVisualStyleBackColor = true;
            // 
            // buttonSetting
            // 
            this.buttonSetting.Location = new System.Drawing.Point(273, 221);
            this.buttonSetting.Name = "buttonSetting";
            this.buttonSetting.Size = new System.Drawing.Size(75, 23);
            this.buttonSetting.TabIndex = 14;
            this.buttonSetting.Text = "Setting";
            this.buttonSetting.UseVisualStyleBackColor = true;
            this.buttonSetting.Click += new System.EventHandler(this.buttonSetting_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(363, 272);
            this.Controls.Add(this.buttonSetting);
            this.Controls.Add(this.groupEmotion);
            this.Controls.Add(this.buttonGenerate);
            this.Controls.Add(this.rtbTextInput);
            this.Name = "Form1";
            this.Text = "TTS Indonesian Emotion";
            this.groupEmotion.ResumeLayout(false);
            this.groupEmotion.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.RichTextBox rtbTextInput;
        private System.Windows.Forms.Button buttonGenerate;
        private System.Windows.Forms.RadioButton radioHappy;
        private System.Windows.Forms.RadioButton radioAngry;
        private System.Windows.Forms.RadioButton radioSad;
        private System.Windows.Forms.GroupBox groupEmotion;
        private System.Windows.Forms.RadioButton radioNormal;
        private System.Windows.Forms.Button buttonSetting;
    }
}


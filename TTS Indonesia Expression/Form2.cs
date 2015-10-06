using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Globalization;

namespace TTS_Indonesia_Expression
{
    public partial class Form2 : Form
    {
        public Form2()
        {
            InitializeComponent();
        }

        /* ========== Write File ========== */
        private void WriteHappy(string str)
        {
            File.WriteAllText(@"C:\DataCounter\happy.txt", str);
        }

        private void WriteAngry(string str)
        {
            File.WriteAllText(@"C:\DataCounter\angry.txt", str);
        }

        private void WriteSad(string str)
        {
            File.WriteAllText(@"C:\DataCounter\sad.txt", str);
        }

        /* ========== Read File ========== */
        private List<int> ReadHappy()
        {
            List<int> values = new List<int>();

            using (StreamReader sr = new StreamReader(@"C:\DataCounter\happy.txt", true))
            {
                string lines;
                while ((lines = sr.ReadLine()) != null)
                {
                    if (lines != "")
                    {
                        string[] line = lines.Split(new char[] { '\t' }, StringSplitOptions.RemoveEmptyEntries); 
                        int value = int.Parse(line[0]);

                        values.Add(value); 
                    } 
                } 
            }

            return values;
        }

        private List<int> ReadAngry()
        {
            List<int> values = new List<int>();

            using (StreamReader sr = new StreamReader(@"C:\DataCounter\angry.txt", true))
            {
                string lines;
                while ((lines = sr.ReadLine()) != null)
                {
                    if (lines != "")
                    {
                        string[] line = lines.Split(new char[] { '\t' }, StringSplitOptions.RemoveEmptyEntries); 
                        int value = int.Parse(line[0]);

                        values.Add(value); 
                    } 
                } 
            }

            return values;
        }

        private List<int> ReadSad()
        {
            List<int> values = new List<int>();

            using (StreamReader sr = new StreamReader(@"C:\DataCounter\sad.txt", true))
            {
                string lines;
                while ((lines = sr.ReadLine()) != null)
                {
                    if (lines != "")
                    {
                        string[] line = lines.Split(new char[] { '\t' }, StringSplitOptions.RemoveEmptyEntries); 
                        int value = int.Parse(line[0]);

                        values.Add(value); 
                    } 
                } 
            }

            return values;
        }

        private void buttonLoad_Click(object sender, EventArgs e)
        {
            List<int> values = new List<int>();

            if ((comboEmotion.Text == "Happy") || (comboEmotion.Text == "Angry") || (comboEmotion.Text == "Sad"))
            {
                if (comboEmotion.Text == "Happy")
                {
                    values = ReadHappy();
                }
                else if (comboEmotion.Text == "Angry")
                {
                    values = ReadAngry();
                }
                else if (comboEmotion.Text == "Sad")
                {
                    values = ReadSad();
                }

                textBox0.Text = values[0].ToString();
                textBox1.Text = values[1].ToString();
                textBox2.Text = values[2].ToString();
                textBox3.Text = values[3].ToString();
                textBox4.Text = values[4].ToString();
                textBox5.Text = values[5].ToString();
                textBox6.Text = values[6].ToString();
                textBox7.Text = values[7].ToString();
                textBox8.Text = values[8].ToString();
                textBox9.Text = values[9].ToString();
                textBox10.Text = values[10].ToString();
                textBox11.Text = values[11].ToString();
                textBox12.Text = values[12].ToString();
                textBox13.Text = values[13].ToString();
                textBox14.Text = values[14].ToString();
                textBox15.Text = values[15].ToString();
                textBox16.Text = values[16].ToString();
                textBox17.Text = values[17].ToString();
                textBox18.Text = values[18].ToString();
                textBox19.Text = values[19].ToString();
                textBox20.Text = values[20].ToString();
                textBox21.Text = values[21].ToString();
                textBox22.Text = values[22].ToString();
                textBox23.Text = values[23].ToString();
                textBox24.Text = values[24].ToString();
                textBox25.Text = values[25].ToString();
                textBox26.Text = values[26].ToString();
                textBox27.Text = values[27].ToString();
                textBox28.Text = values[28].ToString();
                textBox29.Text = values[29].ToString();
                textBox30.Text = values[30].ToString();
                textBox31.Text = values[31].ToString();
                textBox32.Text = values[32].ToString();
            }
        }

        private void buttonSave_Click(object sender, EventArgs e)
        {
            List<string> values = new List<string>();
            
            if ((comboEmotion.Text == "Happy") || (comboEmotion.Text == "Angry") || (comboEmotion.Text == "Sad"))
            {
                string str = textBox0.Text + "\n" + textBox1.Text + "\n" + textBox2.Text + "\n\n" +
                  textBox3.Text + "\n" + textBox4.Text + "\n" + textBox5.Text + "\n\n" +
                  textBox6.Text + "\n" + textBox7.Text + "\n" + textBox8.Text + "\n\n" +
                  textBox9.Text + "\n" + textBox10.Text + "\n" + textBox11.Text + "\n\n" +
                  textBox12.Text + "\n" + textBox13.Text + "\n" + textBox14.Text + "\n\n" +
                  textBox15.Text + "\n" + textBox16.Text + "\n" + textBox17.Text + "\n\n" +
                  textBox18.Text + "\n\n" +
                  textBox19.Text + "\n" + textBox20.Text + "\n\n" +
                  textBox21.Text + "\n" + textBox22.Text + "\n\n" +
                  textBox23.Text + "\n" + textBox24.Text + "\n\n" +
                  textBox25.Text + "\n" + textBox26.Text + "\n\n" +
                  textBox27.Text + "\n" + textBox28.Text + "\n\n" +
                  textBox29.Text + "\n" + textBox30.Text + "\n\n" +
                  textBox31.Text + "\n" + textBox32.Text;

                if (comboEmotion.Text == "Happy")
                {
                    WriteHappy(str);
                }
                else if (comboEmotion.Text == "Angry")
                {
                    WriteAngry(str);
                }
                else if (comboEmotion.Text == "Sad")
                {
                    WriteSad(str);
                }
            }
        }

        private void buttonCancel_Click(object sender, EventArgs e)
        {
            this.Hide();
            var form2 = new Form2();
            form2.Closed += (s, args) => this.Close();
        }

    }
}

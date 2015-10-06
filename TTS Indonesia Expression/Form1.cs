using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Text.RegularExpressions;
using System.IO;
using System.Data.OleDb;
using System.Diagnostics;

namespace TTS_Indonesia_Expression
{
    public partial class Form1 : Form
    {
        /* ========== Variable ========== */
        private OleDbConnection connection = new OleDbConnection();
        private OleDbCommand command = new OleDbCommand();
        
        private static string[] vocal = {"V", "e", "@", "I", "Q", "U", "aI", "OI", "aU"};

        public Form1()
        {
            InitializeComponent();
            connection.ConnectionString = @"Provider=Microsoft.ACE.OLEDB.12.0;Data Source=C:\DataCounter\IndonesianDictionary.accdb;Persist Security Info=False";
        }

        private string CreatePho(string sentence)
        {
         
            string espeak = string.Format(@"espeak -v mb-id1 -q --pho ""{0}""", sentence); 

           
            ProcessStartInfo cmd = new ProcessStartInfo("cmd", "/c " + espeak);
            cmd.WorkingDirectory = @"C:\Program Files (x86)\eSpeak\command_line\";
            cmd.RedirectStandardOutput = true; 
            cmd.UseShellExecute = false;
            cmd.CreateNoWindow = true;

            
            Process proc = new Process();
            proc.StartInfo = cmd;
            proc.Start();

            string result = proc.StandardOutput.ReadToEnd();

            return result;
        } 

        /* ========== Database "Kamus" ========== */
        
        private List<String> SearchSampaSyllable(List<String> words) 
        {
            List<String> sampaSyllable = new List<string>();

            foreach (string word in words)
            {
                connection.Open();
                command.Connection = connection;
                command.CommandText = "select * from Kamus where Kata = '" + word + "'";

                
                OleDbDataReader reader = command.ExecuteReader();
                while (reader.Read()) 
                {
                    sampaSyllable.Add(reader.GetString(4));
                }

                connection.Close(); 
            } 

            return sampaSyllable;
        }

        /* ========== Database "Pho" ========== */

        private int InsertPho(string result)
        {
            string insert;
            int i = 0; 
            
           
            using (StreamWriter sw = new StreamWriter("hasil.pho"))
            {
                sw.Write(result);
            }

            using (StreamReader sr = new StreamReader("hasil.pho"))
            {
                string lines;
                while ((lines = sr.ReadLine()) != null) 
                {
                    if (lines != "") 
                    {
                        i++; 
                        string[] line = lines.Split(new char[] { '\t' }, StringSplitOptions.RemoveEmptyEntries); 
                        insert = string.Format("insert into Pho (ID,Sampa,Durasi,Pitch) Values ('{0}','{1}','{2}', ' ')", i, line[0], line[1]); 

                        if (line.Count() == 3)
                        {
                            insert = string.Format("insert into Pho (ID,Sampa,Durasi,Pitch) Values ('{0}','{1}','{2}','{3}')", i, line[0], line[1], line[2]); 
                        }

                       
                        connection.Open(); 
                        command.Connection = connection;
                        command.CommandText = insert; 

                        command.ExecuteNonQuery();

                        connection.Close(); 
                    } 
                } 
            }

            return i;
        } 

        private void DeletePho()
        {
            string update = "delete from pho";

            
            connection.Open(); 
            command.Connection = connection;
            command.CommandText = update; 

            command.ExecuteNonQuery(); 

            connection.Close(); 
        }

        private string SearchSampa(int index)
        {
            string sampaLetter = "";
            string select = string.Format("select * from Pho where ID = {0}", index);

            connection.Open(); 
            command.Connection = connection;
            command.CommandText = select; 

            
            OleDbDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                sampaLetter = reader.GetString(1);
            }

            connection.Close(); 

            return sampaLetter;
        } 

        private string SearchDuration(int index)
        {
            string sampaDuration = "";
            string select = string.Format("select * from Pho where ID = {0}", index);

            connection.Open(); 
            command.Connection = connection;
            command.CommandText = select; 

            
            OleDbDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                sampaDuration = reader.GetString(2);
            }

            connection.Close(); 
            
            return sampaDuration;
        } 

        private string SearchPitch(int index)
        {
            string sampaPitch = "";
            string select = string.Format("select * from Pho where ID = {0}", index);

            connection.Open(); 
            command.Connection = connection;
            command.CommandText = select; 

            
            OleDbDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                sampaPitch = reader.GetString(3);
            }
            sampaPitch = sampaPitch.Trim();

            connection.Close(); 

            return sampaPitch;
        } 

        private void UpdateDuration(int index, int duration)
        {
            string update = string.Format("update Pho set Durasi = '{0}' where ID = {1}", duration, index);

            
            connection.Open(); 
            command.Connection = connection;
            command.CommandText = update; 

            command.ExecuteNonQuery(); 

            connection.Close(); 
        } 

        private void UpdatePitch(int index, string pitch)
        {
            string update = string.Format("update Pho Set Pitch = '{0}' where ID = {1}", pitch, index);

           
            connection.Open(); 
            command.Connection = connection;
            command.CommandText = update;

            command.ExecuteNonQuery(); 

            connection.Close(); 
        } 

        private string GetPhoSampa(int id)
        {
            string file = "";

            string select = string.Format("select * from Pho where ID = {0}", id);

            connection.Open(); 
            command.Connection = connection;
            command.CommandText = select; 

            // search in database
            OleDbDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                file = reader.GetString(1);
            }

            connection.Close(); // close connection to ms.access

            return file;
        }

        private string GetPhoDuration(int id)
        {
            string file = "";

            string select = string.Format("select * from Pho where ID = {0}", id);

            connection.Open(); 
            command.Connection = connection;
            command.CommandText = select; 

            
            OleDbDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                file = reader.GetString(2);
            }

            connection.Close(); 

            return file;
        }

        private string GetPhoPitch(int id)
        {
            string file = "";

            string select = string.Format("select * from Pho where ID = {0}", id);

            connection.Open(); 
            command.Connection = connection;
            command.CommandText = select; 

            
            OleDbDataReader reader = command.ExecuteReader();
            while (reader.Read())
            {
                file = reader.GetString(3);
            }

            connection.Close();

            return file;
        }

        /* ========== Read File ========== */
        private List<double> ReadHappy()
        {
            List<double> values = new List<double>();

            using (StreamReader sr = new StreamReader(@"C:\DataCounter\happy.txt", true))
            {
                string lines;
                while ((lines = sr.ReadLine()) != null)
                {
                    if (lines != "")
                    {
                        string[] line = lines.Split(new char[] { '\t' }, StringSplitOptions.RemoveEmptyEntries); 
                        double value = int.Parse(line[0]);
                        
                        value = value / 100;

                        values.Add(value);
                    } 
                } 
            }

            return values;
        }

        private List<double> ReadAngry()
        {
            List<double> values = new List<double>();

            using (StreamReader sr = new StreamReader(@"C:\DataCounter\angry.txt", true))
            {
                string lines;
                while ((lines = sr.ReadLine()) != null)
                {
                    if (lines != "")
                    {
                        string[] line = lines.Split(new char[] { '\t' }, StringSplitOptions.RemoveEmptyEntries); 
                        double value = int.Parse(line[0]);
                        
                        value = value / 100;

                        values.Add(value); 
                    }
                } 
            }

            return values;
        }

        private List<double> ReadSad()
        {
            List<double> values = new List<double>();

            using (StreamReader sr = new StreamReader(@"C:\DataCounter\sad.txt", true))
            {
                string lines;
                while ((lines = sr.ReadLine()) != null)
                {
                    if (lines != "")
                    {
                        string[] line = lines.Split(new char[] { '\t' }, StringSplitOptions.RemoveEmptyEntries); 
                        double value = int.Parse(line[0]);

                        value = value / 100;

                        values.Add(value); 
                    } 
                } 
            }

            return values;
        }

        /* ========== Emotion ========== */
        private void Emotion(List<String> sampaSyllable, List<double> values)
        {
            int index = 1; 
            
            // calculate duration
            for (int i = 0; i < sampaSyllable.Count(); i++)
            {
                string[] syllable = sampaSyllable[i].Split('-'); 

                if (i == 0) 
                {
                    for (int idx = 0; idx < syllable.Count(); idx++)
                    {
                        string[] letter = syllable[idx].Split('.'); 

                        if (idx == 0) 
                        {
                            int cons = 0;

                            for (int indx = 0; indx < letter.Count(); indx++)
                            {
                                
                                string sam = SearchSampa(index);
                                while (letter[indx] != sam)
                                {
                                    index++;
                                    sam = SearchSampa(index);
                                }

                                
                                if (vocal.Any(letter[indx].Contains))
                                {
                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    duration += Convert.ToInt32(duration * values[2]);
                                    UpdateDuration(index, duration); 
                                    
                                    
                                    string value = SearchPitch(index);
                                    int[] pitchValue = value.Split(' ').Select(s => int.Parse(s)).ToArray();
                                    
                                    pitchValue[1] += Convert.ToInt32(pitchValue[1] * values[19]);
                                    pitchValue[pitchValue.Count() - 1] += Convert.ToInt32(pitchValue[pitchValue.Count() - 1] * values[20]);
                                    
                                    string pitch = "";
                                    foreach (var item in pitchValue)
                                    {
                                        pitch += " " + item;
                                    }
                                    UpdatePitch(index, pitch);
                                }
                                else
                                {
                                    cons++;

                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    if (cons == 1) 
                                    {
                                        duration += Convert.ToInt32(duration * values[0]);
                                    }
                                    else if (cons == 2) 
                                    {
                                        duration += Convert.ToInt32(duration * values[1]);
                                    }
                                    UpdateDuration(index, duration); 
                                    UpdatePitch(index, " ");
                                }

                                index++;
                            }
                        }
                        else if (idx == (syllable.Count() - 1)) 
                        {
                            int cons = 0;

                            for (int indx = 0; indx < letter.Count(); indx++)
                            {
                                
                                string sam = SearchSampa(index);
                                while (letter[indx] != sam)
                                {
                                    index++;
                                    sam = SearchSampa(index);
                                }

                                
                                if (vocal.Any(letter[indx].Contains))
                                {
                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    duration += Convert.ToInt32(duration * values[8]);
                                    UpdateDuration(index, duration); 

                                    
                                    string value = SearchPitch(index);
                                    int[] pitchValue = value.Split(' ').Select(s => int.Parse(s)).ToArray();

                                    pitchValue[1] += Convert.ToInt32(pitchValue[1] * values[23]);
                                    pitchValue[pitchValue.Count() - 1] += Convert.ToInt32(pitchValue[pitchValue.Count() - 1] * values[24]);
                                    
                                    string pitch = "";
                                    foreach (var item in pitchValue)
                                    {
                                        pitch += " " + item;
                                    }
                                    UpdatePitch(index, pitch);
                                }
                                else
                                {
                                    cons++;

                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    if (cons == 1) 
                                    {
                                        duration += Convert.ToInt32(duration * values[6]);
                                    }
                                    else if (cons == 2) 
                                    {
                                        duration += Convert.ToInt32(duration * values[7]);
                                    }
                                    UpdateDuration(index, duration); 
                                    UpdatePitch(index, " ");
                                }

                                index++;
                            }
                        }
                        else 
                        {
                            int cons = 0;

                            for (int indx = 0; indx < letter.Count(); indx++)
                            {
                                
                                string sam = SearchSampa(index);
                                while (letter[indx] != sam)
                                {
                                    index++;
                                    sam = SearchSampa(index);
                                }

                                
                                if (vocal.Any(letter[indx].Contains))
                                {
                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    duration += Convert.ToInt32(duration * values[5]);
                                    UpdateDuration(index, duration); 

                                    
                                    string value = SearchPitch(index);
                                    int[] pitchValue = value.Split(' ').Select(s => int.Parse(s)).ToArray();

                                    pitchValue[1] += Convert.ToInt32(pitchValue[1] * values[21]);
                                    pitchValue[pitchValue.Count() - 1] += Convert.ToInt32(pitchValue[pitchValue.Count() - 1] * values[22]);
                                    
                                    string pitch = "";
                                    foreach (var item in pitchValue)
                                    {
                                        pitch += " " + item;
                                    }
                                    UpdatePitch(index, pitch);
                                }
                                else
                                {
                                    cons++;

                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    if (cons == 1) 
                                    {
                                        duration += Convert.ToInt32(duration * values[3]);
                                    }
                                    else if (cons == 2) 
                                    {
                                        duration += Convert.ToInt32(duration * values[4]);
                                    }
                                    UpdateDuration(index, duration); 
                                    UpdatePitch(index, " ");
                                }

                                index++;
                            }
                        }
                    }
                } 
                else if (i == (sampaSyllable.Count() - 1)) 
                {
                    for (int idx = 0; idx < syllable.Count(); idx++)
                    {
                        string[] letter = syllable[idx].Split('.'); 

                        if (idx == 0) 
                        {
                            int cons = 0;

                            for (int indx = 0; indx < letter.Count(); indx++)
                            {
                                
                                string sam = SearchSampa(index);
                                while (letter[indx] != sam)
                                {
                                    index++;
                                    sam = SearchSampa(index);
                                }

                                
                                if (vocal.Any(letter[indx].Contains))
                                {
                                    
                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    duration += Convert.ToInt32(duration * values[11]);
                                    UpdateDuration(index, duration); 
                                    

                                    
                                    string value = SearchPitch(index);
                                    
                                    int[] pitchValue = value.Split(' ').Select(s => int.Parse(s)).ToArray();

                                    pitchValue[1] += Convert.ToInt32(pitchValue[1] * values[25]);
                                    pitchValue[pitchValue.Count() - 1] += Convert.ToInt32(pitchValue[pitchValue.Count() - 1] * values[26]);
                                    
                                    string pitch = "";
                                    foreach (var item in pitchValue)
                                    {
                                        pitch += " " + item;
                                    }
                                    UpdatePitch(index, pitch);
                                }
                                else
                                {
                                    cons++;

                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    if (cons == 1) 
                                    {
                                        duration += Convert.ToInt32(duration * values[9]);
                                    }
                                    else if (cons == 2) 
                                    {
                                        duration += Convert.ToInt32(duration * values[10]);
                                    }
                                    UpdateDuration(index, duration); 
                                    UpdatePitch(index, " ");
                                }

                                index++;
                            }
                        }
                        else if (idx == (syllable.Count() - 1)) 
                        {
                            int cons = 0;

                            for (int indx = 0; indx < letter.Count(); indx++)
                            {
                                
                                string sam = SearchSampa(index);
                                while (letter[indx] != sam)
                                {
                                    index++;
                                    sam = SearchSampa(index);
                                }

                                
                                if (vocal.Any(letter[indx].Contains))
                                {
                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    duration += Convert.ToInt32(duration * values[17]);
                                    UpdateDuration(index, duration); 

                                    
                                    string value = SearchPitch(index);
                                    int[] pitchValue = value.Split(' ').Select(s => int.Parse(s)).ToArray();

                                    pitchValue[1] += Convert.ToInt32(pitchValue[1] * values[29]);
                                    pitchValue[pitchValue.Count() - 1] += Convert.ToInt32(pitchValue[pitchValue.Count() - 1] * values[30]);
                                    
                                    string pitch = "";
                                    foreach (var item in pitchValue)
                                    {
                                        pitch += " " + item;
                                    }
                                    UpdatePitch(index, pitch);
                                }
                                else
                                {
                                    cons++;

                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    if (cons == 1) 
                                    {
                                        duration += Convert.ToInt32(duration * values[15]);
                                    }
                                    else if (cons == 2) 
                                    {
                                        duration += Convert.ToInt32(duration * values[16]);
                                    }
                                    UpdateDuration(index, duration); 
                                    UpdatePitch(index, " ");
                                }

                                index++;
                            }
                        }
                        else 
                        {
                            int cons = 0;

                            for (int indx = 0; indx < letter.Count(); indx++)
                            {
                                
                                string sam = SearchSampa(index);
                                while (letter[indx] != sam)
                                {
                                    index++;
                                    sam = SearchSampa(index);
                                }

                                
                                if (vocal.Any(letter[indx].Contains))
                                {
                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    duration += Convert.ToInt32(duration * values[14]);
                                    UpdateDuration(index, duration); 

                                    
                                    string value = SearchPitch(index);
                                    int[] pitchValue = value.Split(' ').Select(s => int.Parse(s)).ToArray();

                                    pitchValue[1] += Convert.ToInt32(pitchValue[1] * values[27]);
                                    pitchValue[pitchValue.Count() - 1] += Convert.ToInt32(pitchValue[pitchValue.Count() - 1] * values[28]);
                                    
                                    string pitch = "";
                                    foreach (var item in pitchValue)
                                    {
                                        pitch += " " + item;
                                    }
                                    UpdatePitch(index, pitch);
                                }
                                else
                                {
                                    cons++;

                                    
                                    string val = SearchDuration(index);
                                    int duration = int.Parse(val);
                                    if (cons == 1) 
                                    {
                                        duration += Convert.ToInt32(duration * values[12]);
                                    }
                                    else if (cons == 2) 
                                    {
                                        duration += Convert.ToInt32(duration * values[13]);
                                    }
                                    UpdateDuration(index, duration); 
                                    UpdatePitch(index, " ");
                                }

                                index++;
                            }
                        }
                    }
                } 
                else 
                {
                    for (int idx = 0; idx < syllable.Count(); idx++)
                    {
                        string[] letter = syllable[idx].Split('.'); 

                        for (int indx = 0; indx < letter.Count(); indx++)
                        {
                            
                            string sam = SearchSampa(index);
                            while (letter[indx] != sam)
                            {
                                index++;
                                sam = SearchSampa(index);
                            }

                            
                            string val = SearchDuration(index);
                            int duration = int.Parse(val);

                            duration += Convert.ToInt32(duration * values[18]);
                            UpdateDuration(index, duration); 

                            
                            if (vocal.Any(letter[indx].Contains))
                            {
                                
                                string value = SearchPitch(index);
                                int[] pitchValue = value.Split(' ').Select(s => int.Parse(s)).ToArray();

                                pitchValue[1] += Convert.ToInt32(pitchValue[1] * values[31]);
                                pitchValue[pitchValue.Count() - 1] += Convert.ToInt32(pitchValue[pitchValue.Count() - 1] * values[32]);
                                
                                string pitch = "";
                                foreach (var item in pitchValue)
                                {
                                    pitch += " " + item;
                                }
                                UpdatePitch(index, pitch);
                            }
                            else
                            {
                                UpdatePitch(index, " ");
                            }

                            index++;
                        }
                    }
                } 
            }
        }

        /* ========== Generate Button ========== */
        private void buttonGenerate_Click(object sender, EventArgs e)
        {
            DeletePho(); 

            string sentence = rtbTextInput.Text.Trim(); 

            
            string result = CreatePho(sentence); 
            int totalId = InsertPho(result); 

            
            

            List<String> words = sentence.Split(' ').ToList(); 
            List<String> sampaSyllable = SearchSampaSyllable(words); 

            // check emotion
            if (radioHappy.Checked)
            {
                List<double> values = ReadHappy();
                Emotion(sampaSyllable, values);
            }
            else if (radioAngry.Checked)
            {
                List<double> values = ReadAngry();
                Emotion(sampaSyllable, values);
            }
            else if (radioSad.Checked)
            {
                List<double> values = ReadSad();
                Emotion(sampaSyllable, values);
            }

            
            string file = "";
            for (int id = 1; id < totalId+1; id++)
            {
                file += GetPhoSampa(id) + "\t";
                file += GetPhoDuration(id) + "\t";
                file += GetPhoPitch(id);
                file = file.Trim();
                file += "\n";
            }

            
            string path = @"C:\OUTPUT-PHO\hasil.pho";
            File.WriteAllText(path, file);

            
            DeletePho();


            MessageBox.Show("Done");
        }

        private void rtbInput_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                e.Handled = true;
            }
        } 

        private void buttonSetting_Click(object sender, EventArgs e)
        {
            Form2 frm = new Form2();
            frm.Show();
        }
    }
}

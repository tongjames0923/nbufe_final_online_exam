using Examer_Client.Objects;
using System.Collections.Generic;
using System.Windows.Controls;

namespace Examer_Client.Utils
{
    public class SystemManager
    {
        private static SystemManager manager = new SystemManager();
        Feature feature = new Feature();
        private SystemManager()
        {

        }
        public static SystemManager Manager
        {
            get { return manager; }
            private set { manager = value; }
        }
        public ExamUser ExamUser
        {
            get;
            set;
        }

        public List<ExamQuestion> Questions
        {
            get
            {
                return Exam.questions;
            }
        }
        public Feature Feature
        {
            get { return feature; }
        }
        public Frame WindowArea
        {
            get;
            set;
        }
        private ExamPost exam = null;
        Dictionary<int, CheckData> checks;


        public CheckData getByQuestionId(int ques)
        {
            return checks[ques];
        }

        public List<CheckData> AllChecks
        {
            get
            {
                List<CheckData> d = new List<CheckData>();
                foreach (var i in checks)
                {
                    d.Add(i.Value);
                }
                return d;
            }
        }
        public ExamInfo ExamInfo
        {
            get;
            set;
        }
        public ExamPost Exam
        {
            get => exam;
            set
            {
                checks = new Dictionary<int, CheckData>();
                for (int i = 0; i < value.questions.Count; i++)
                {
                    CheckData d = new CheckData();
                    d.queid = value.questions[i].ques_id;
                    d.text = new List<string>();
                    checks.Add(d.queid, d);
                }
                exam = value;
            }
        }
    }
}

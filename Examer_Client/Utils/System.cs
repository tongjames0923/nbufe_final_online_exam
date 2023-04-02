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

        public ExamPost Exam
        {
            get;
            set;
        }
    }
}

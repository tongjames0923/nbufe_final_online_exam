using System;
using System.Collections.Generic;

namespace Examer_Client.Objects
{
    public class NetResp<T>
    {
        public int code
        {
            get;
            set;
        }
        public T data
        {
            get;
            set;
        }
        public String message
        {
            get;
            set;
        }
    }
    public class ExamInfo
    {
        /// <summary>
        /// 
        /// </summary>
        public int exam_id { get; set; }
        /// <summary>
        /// Python 简单考试
        /// </summary>
        public string exam_name { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public string exam_begin { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public int exam_len { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public string exam_file { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public string exam_note { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public int exam_status { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public int readable { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public int writeable { get; set; }
        /// <summary>
        /// 
        /// </summary>
        public int checkable { get; set; }
    }

    public class ExamPost
    {
        public String exam_name
        {
            get;
            set;
        }
        public String exam_begin
        {
            get;
            set;
        }
        public int exam_len
        {
            get;
            set;
        }
        public String exam_note
        {
            get; set;
        }

        public List<ExamUser> students
        {
            get;
            set;
        }
        public List<ExamQuestion> questions
        {
            get;
            set;
        }
    }

    public class ExamQuestion
    {
        public double score
        {
            get;
            set;
        }
        public int ques_id
        {
            get; set;
        }
        public Question detail
        {
            get;
            set;
        }
    }

    public class Question
    {
        public int que_id
        {
            get;
            set;
        }
        public int que_type
        {
            get;
            set;
        }
        public String title
        {
            get;
            set;
        }


        public String answer_data
        {
            get;
            set;
        }
        public int que_creator
        {
            get;
            set;
        }
        public String que_alter_time
        {
            get;
            set;
        }
        public String que_file
        {
            get;
            set;
        }
        public int publicable
        {
            get;
            set;
        }
        public int use_time
        {
            get;
            set;
        }
        public int answerd
        {
            get;
            set;
        }
        public float answerd_right
        {
            get;
            set;
        }
    }

    public class ExamUser
    {
        public String id
        {
            get;
            set;
        }
        public String number
        {
            get;
            set;
        }
        public String name
        {
            get;
            set;
        }
    }
}

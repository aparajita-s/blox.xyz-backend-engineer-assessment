# blox.xyz-backend-engineer-assessment
Recruitment assessment for the Backend Engineer role at Blox.xyz. Includes solutions to json parser and distributed banking problems, alongside an overview of past academic projects.

# Question 1 - Mandatory
# Elaborate what your internship or academic projects were?
1. Dashboard for Library
~ The tool significantly enhanced workflow efficiency by automating the traditionally manual process of reviewing e-book content to assess topic relevance. This automation reduced the effort required by 60%, allowing users to quickly determine whether a book addresses their areas of interest, thereby saving time and improving productivity. The dashboard's intuitive interface further streamlined access to relevant e-books, making it a valuable resource for researchers, students, and professionals.

2. Sign Language Interpreter
~ The primary objective of this innovation was to bridge the communication gap for the deaf and hard-of-hearing community. By providing an automated translation system, the model significantly reduced the language barrier, making communication more accessible and inclusive.

# a. What did the system do?
1. Dashboard for Library
~ Developed an advanced dashboard designed to efficiently identify and track the availability of e-books related to specific topics by implementing sentence tokenization techniques. The system analyzes text content within e-books, breaking it into meaningful segments to evaluate their relevance to user-defined topics. This approach ensures accurate categorization and topic identification, making it easier to locate resources aligned with specific interests or research areas.

2. Sign Language Interpreter
~ Developed a real-time object detection model utilizing Deep Learning and the Single Shot MultiBox Detector (SSD) architecture to translate sign language gestures into text. The model was designed to recognize and interpret hand movements and gestures commonly used in sign language, converting them into corresponding textual outputs in real time. This solution involved training the SSD model on a comprehensive dataset of sign language gestures, ensuring high accuracy in detecting and interpreting diverse signs across various conditions.

# b. What other systems have you seen in the wild like that?
~ 

# c. How do you approach the development problem?
~ I break down the problem into smaller modules, preferably designing it in visual structure. Using the topology diagram, I try to draft psuedocode which I test with dry runs. With each dry run, I try to find a more efficient/optimized manner to implement it.

# d. What were interesting aspects where you copied code from Stack Overflow?
~ I came across Memory Leak issue in Java for the first time in an Enterprive Java Application, while I was on a scavenger hunt to find the one piece of code in the project from where I could resolve this issue, I found that we can handle this issue through Tomcat and luckily, the project was deployed on Apache Tomcat.

# e. What did you learn from some very specific copy paste? Mention explicitly some of them. 
~ Xms is a Java Virtual Machine option that specifies the initial heap size allocated to the JVM at startup. It can improve performance for applications with predictable memory usage by avoiding the need for dynamic memory allocation during runtime. It is useful in high-performance or production environments where consistent memory allocation is desired.

~ Copied 
- set CATALINA_OPTS=-Xms512m -Xmx512m

# Question 2 - Refer json-parser project
# Question 5 - Refer distributed-banking-basic-approach and distributed-banking-enhanced-approach
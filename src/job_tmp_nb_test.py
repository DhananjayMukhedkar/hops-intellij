from pyspark.sql import SparkSession
from pyspark.sql import SQLContext
spark=SparkSession.builder.enableHiveSupport().getOrCreate()
sc=spark.sparkContext
sqlContext=SQLContext(sc)
#!/usr/bin/env python
# coding: utf-8

# # Interacting with HopsFS
# 
# HopsFS is a fork of the Hadoop Distributed File System (HDFS). 
# 
# To see what distinguishes HopsFS from HDFS from an architecural point of view refer to:
# 
# - [blogpost](https://www.logicalclocks.com/introducing-hops-hadoop/)
# - [papers](https://www.logicalclocks.com/research-papers/)
# 
# To interact with HopsFS from python, you can use the hdfs module in the hops-util-py library, it provides an easy-to-use API that resembles interaction with the local filesystem using the python `os` module. 

# ## Import the Module

# In[ ]:


from hops import hdfs


# ## Getting Project Information
# 
# When interacting with HopsFS from Hopsworks, you are always inside a **project**. When you are inside a project your activated HDFS user will be projectname__username. This is to set project-specific access control and multi-tenancy (you can read more about the low-level details here: [hopsworks blogpost](https://www.logicalclocks.com/introducing-hopsworks/)

# In[ ]:


project_user = hdfs.project_user()
project_name = hdfs.project_name()
project_path = hdfs.project_path()
print("project user: {}\nproject name: {}\nproject path: {}".format(project_user, project_name, project_path))


# ## Read/Write From/To HDFS

# In[ ]:


logs_README = hdfs.load("Logs/README.md")
print("logs README: {}".format(logs_README.decode("utf-8")))
hdfs.dump("test", "Logs/README_dump_test.md")
logs_README_dumped = hdfs.load("Logs/README_dump_test.md")
print("logs README dumped: {}".format(logs_README_dumped.decode("utf-8")))


# ## Copy Local FS <--> HDFS

# In[ ]:


# creates file in current working directory with a string
with open('test.txt', 'w') as f:
    f.write("test")
hdfs.copy_to_hdfs("test.txt", "Resources", overwrite=True)
hdfs.copy_to_local("Resources/test.txt", overwrite=True)
hdfs_copied_file = hdfs.load("Resources/test.txt")
with open('test.txt', 'r') as f:
    local_copied_file = f.read()
print("copied file from local to hdfs: {}".format(hdfs_copied_file.decode("utf-8")))
print("copied file from hdfs to local: {}".format(local_copied_file))


# ## List Directories

# In[ ]:


logs_files = hdfs.ls("Logs/")
print(logs_files)
logs_files_md = hdfs.glob("Logs/*.md")
print(logs_files_md)
logs_path_names = hdfs.lsl("Logs/")
print(logs_path_names)


# ## Copy Within HDFS

# In[ ]:


hdfs.cp("Resources/test.txt", "Logs/")
logs_files = hdfs.ls("Logs/")
print(logs_files)


# ## Create and Remove Directories

# In[ ]:


hdfs.mkdir("Logs/test_dir")
logs_files_prior_delete = hdfs.ls("Logs/")
print("files prior to delete: {}".format(logs_files_prior_delete))
hdfs.rmr("Logs/test_dir")
logs_files_after_delete = hdfs.ls("Logs/")
print("files after to delete: {}".format(logs_files_after_delete))


# ## Move/Rename Files

# In[ ]:


logs_files_prior_move = hdfs.ls("Logs/")
print("files prior to move: {}".format(logs_files_prior_move))
hdfs.move("Logs/README_dump_test.md", "Logs/README_dump_test2.md")
logs_files_after_move = hdfs.ls("Logs/")
print("files after move: {}".format(logs_files_after_move))
logs_files_prior_rename = hdfs.ls("Logs/")
print("files prior to rename: {}".format(logs_files_prior_rename))
hdfs.rename("Logs/README_dump_test2.md", "Logs/README_dump_test.md")
logs_files_after_rename = hdfs.ls("Logs/")
print("files after move: {}".format(logs_files_after_rename))


# ## Change Owner and Change Mode

# In[ ]:


import stat
file_stat = hdfs.stat("Logs/README.md")
print("file permissions prior to chmod: {0:b}".format(file_stat.st_mode))
hdfs.chmod("Logs/README.md", 700)
file_stat = hdfs.stat("Logs/README.md")
print("file permissions after to chmod: {0:b}".format(file_stat.st_mode))
hdfs.chmod("Logs/README.md", 777)
file_owner = file_stat.st_uid
#print("file owner prior to chown: {}".format(file_owner))
#hdfs.chown("Logs/README.md", "meb10000", "meb10000")


# ## File Metadata

# In[ ]:


file_stat = hdfs.stat("Logs/README.md")
print("file_stat: {}".format(file_stat))
file_access = hdfs.access("Logs/README.md", 777)
print("file access: {}".format(file_access))


# ## Get Absolute Path

# In[ ]:


abs_path = hdfs.abs_path("Logs/")
print(abs_path)


# ## Check if file/folder exists

# In[ ]:


hdfs.exists("Logs/")


# In[ ]:


hdfs.exists("Not_Existing/neither_am_i")

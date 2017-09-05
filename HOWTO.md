# How-tos

This documents gives short instructions for different development practices.

## Set up local project
To set up a local copy of the project please follow this short list of instructions.
1. Open IntelliJ IDEA
2. Click: File -> New -> Project from Version Control -> Github
3. Enter Credentials:
    > **Git Repository URL**: https://github.com/AudiophileDev/t2m.git <br>
    **Parent Directory**: Choose as you wish <br>
    **Directory Name**: `t2m`
4. Click Clone
5. Right click on folder `src` -> Mark Directory as -> Sources Root
6. Open `src/com/audiophile/t2m/Main.java`
7. Click on **Setup SDK**
8. Choose Java 1.8 and click OK
9. Go to File -> Project Structure... and insert Project Compiler output:
    > `"project_directory"/out` e.g. "C:/Users/MyUser/IdeaProjects/t2m/out"
10. Change Project Language Level to "8 - Lambdas, Type annotations etc."
11. Close Project Structure dialog
12. Right click on Main.java -> Run 'Main.main()'

## Make a commit

When making a commit to the repository please adhere to the following instructions.

1. Go to VCS -> Commit... (STRG + K ,⌘ + K on Mac)
2. Select the files you want to commit
3. Add Commit Message with the following structure:
    > Heading in first line <br>
       \- Changes as bullet points <br>
       \- Put space before and after the '-' character <br>
       \- Indent sub points with 4 spaces
4. Click Commit
5. Go to VCS -> Git -> Push... and click Push
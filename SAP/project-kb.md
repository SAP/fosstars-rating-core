# SAP/project-kb

https://github.com/SAP/project-kb

Last updated on Oct 13, 2021

**Rating**: **BAD**

**Score**: **2.6**, max score value is 10.0

**Confidence**: High (9.87, max confidence value is 10.0)

## Details

The rating is based on **security score for open-source projects**.





It used the following sub-scores:

1.  **[Security testing](#security-testing)**: **0.0** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
            
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        
1.  **[Security awareness](#security-awareness)**: **2.0** (weight is 0.9)
    
1.  **[Vulnerability discovery and security testing](#vulnerability-discovery-and-security-testing)**: **2.0** (weight is 0.6)
    1.  **[Security testing](#security-testing)**: **0.0** (weight is 1.0)
        1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
            1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
                
            1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
                
        1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
            1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
                
            1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
                
            1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
                
        1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
            
        1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
            
        1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
            
1.  **[Unpatched vulnerabilities](#unpatched-vulnerabilities)**: **10.0** (weight is 0.5)
    
1.  **[Community commitment](#community-commitment)**: **8.0** (weight is 0.5)
    
1.  **[Project activity](#project-activity)**: **0.31** (weight is 0.5)
    
1.  **[Project popularity](#project-popularity)**: **0.11** (weight is 0.5)
    
1.  **[Security reviews](#security-reviews)**: **0.0** (weight is 0.2)
    


# ## How to improve the rating

You can ask the project maintainers to enable LGTM checks for pull requests in the project.
More info:
1.  [How to enable LGTM checks for pull requests](https://lgtm.com/help/lgtm/about-automated-code-review)


You can open a pull request to enable CodeQL scans in the project. Make sure that the scans are run on pull requests.
More info:
1.  [How to enable CodeQL checks for pull requests](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)


You can open a pull request to enable CodeQL scans in the project.
More info:
1.  [How to enable CodeQL checks](https://docs.github.com/en/free-pro-team@latest/github/finding-security-vulnerabilities-and-errors-in-your-code/enabling-code-scanning-for-a-repository#enabling-code-scanning-using-actions)


You can open a pull request to enable FindSecBugs for the project.
More info:
1.  [FindSecBugs home page](https://find-sec-bugs.github.io/)


You can enable artifact signing in the project's build pipeline.
More info:
1.  [Apache Maven Jarsigner Plugin](https://maven.apache.org/plugins/maven-jarsigner-plugin/)


You can enable NoHttp tool in the project's build pipeline.
More info:
1.  [NoHttp tool home page](https://github.com/spring-io/nohttp)



## Sub-scores

Below are the details about all the used sub-scores.

### Security testing

Score: **0.0**, confidence is 9.52 (high), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
    1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
        
    1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
        
1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
    1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
        
    1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
        
    1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
        
1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
    
1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
    
1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
    


### Security awareness

Score: **2.0**, confidence is 10.0 (max), weight is 0.9 (high)

The score shows how a project is aware of security. If the project has a security policy, then the score adds 2.00. If the project has a security team, then the score adds 3.00. If the project uses verified signed commits, then the score adds 0.50. If the project has a bug bounty program, then the score adds 4.00. If the project signs its artifacts, then the score adds 0.50. If the project uses a security tool or library, then the score adds 1.00.



This sub-score is based on 17 features:



1.  **Does it have a bug bounty program?** No
1.  **Does it have a security policy?** Yes
1.  **Does it have a security team?** No
1.  **Does it sign artifacts?** No
1.  **Does it use AddressSanitizer?** No
1.  **Does it use Dependabot?** No
1.  **Does it use FindSecBugs?** No
1.  **Does it use LGTM checks?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use OWASP ESAPI?** No
1.  **Does it use OWASP Java Encoder?** No
1.  **Does it use OWASP Java HTML Sanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Does it use nohttp?** No
1.  **Does it use verified signed commits?** No
1.  **How is OWASP Dependency Check used?** Not used
1.  **Is it included to OSS-Fuzz?** No


### Vulnerability discovery and security testing

Score: **2.0**, confidence is 9.76 (high), weight is 0.6 (medium)

The scores checks how security testing is done and how many vulnerabilities were recently discovered. If testing is good, and there are no recent vulnerabilities, then the score value is max. If there are vulnerabilities, then the score value is high. If testing is bad, and there are no recent vulnerabilities, then the score value is low. If there are vulnerabilities, then the score is min.



This sub-score is based on the following sub-score:



1.  **[Security testing](#security-testing)**: **0.0** (weight is 1.0)
    1.  **[Dependency testing](#dependency-testing)**: **N/A** (weight is 1.0)
        1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
            
        1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
            
    1.  **[Static analysis](#static-analysis)**: **0.0** (weight is 1.0)
        1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
            
        1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
            
        1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
            
    1.  **[Fuzzing](#fuzzing)**: **N/A** (weight is 1.0)
        
    1.  **[Memory-safety testing](#memory-safety-testing)**: **N/A** (weight is 1.0)
        
    1.  **[nohttp tool](#nohttp-tool)**: **0.0** (weight is 0.2)
        


This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 36 vulnerabilities, [details below](#known-vulnerabilities)


### Unpatched vulnerabilities

Score: **10.0**, confidence is 10.0 (max), weight is 0.5 (medium)



No unpatched vulnerabilities found which is good

This sub-score is based on 1 feature:



1.  **Info about vulnerabilities in the project:** 36 vulnerabilities, [details below](#known-vulnerabilities)


### Community commitment

Score: **8.0**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 3 features:



1.  **Does it belong to Apache?** No
1.  **Does it belong to Eclipse?** No
1.  **Is it supported by a company?** Yes


### Project activity

Score: **0.31**, confidence is 10.0 (max), weight is 0.5 (medium)

The score evaluates how active a project is. It's based on number of commits and contributors in the last 3 months.

2 commits in the last 3 months results to 0.31 points

This sub-score is based on 2 features:



1.  **Number of commits in the last three months:** 2
1.  **Number of contributors in the last three months:** 1


### Project popularity

Score: **0.11**, confidence is 10.0 (max), weight is 0.5 (medium)

This scoring function is based on number of stars, watchers and dependent projects.



This sub-score is based on 3 features:



1.  **Number of projects on GitHub that use an open source project:** 1
1.  **Number of stars for a GitHub repository:** 64
1.  **Number of watchers for a GitHub repository:** 14


### Security reviews

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)



No security reviews have been done

This sub-score is based on 1 feature:



1.  **Info about security reviews:** 0 security reviews


### Dependency testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[Dependabot score](#dependabot-score)**: **N/A** (weight is 1.0)
    
1.  **[OWASP Dependency Check score](#owasp-dependency-check-score)**: **N/A** (weight is 1.0)
    


### Static analysis

Score: **0.0**, confidence is 8.0 (low), weight is 1.0 (high)





This sub-score is based on the following sub-scores:



1.  **[LGTM score](#lgtm-score)**: **0.0** (weight is 1.0)
    
1.  **[How a project uses CodeQL](#how-a-project-uses-codeql)**: **0.0** (weight is 1.0)
    
1.  **[FindSecBugs score](#findsecbugs-score)**: **N/A** (weight is 0.5)
    


### Fuzzing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Is it included to OSS-Fuzz?** No
1.  **Programming languages:** PYTHON, GO, OTHER


### Memory-safety testing

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use AddressSanitizer?** No
1.  **Does it use MemorySanitizer?** No
1.  **Does it use UndefinedBehaviorSanitizer?** No
1.  **Programming languages:** PYTHON, GO, OTHER


### nohttp tool

Score: **0.0**, confidence is 10.0 (max), weight is 0.2 (low)





This sub-score is based on 2 features:



1.  **Does it use nohttp?** No
1.  **Package managers:** None


### Dependabot score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it use Dependabot?** No
1.  **Does it use GitHub as the main development platform?** Yes
1.  **Package managers:** None
1.  **Programming languages:** PYTHON, GO, OTHER


### OWASP Dependency Check score

Score: **N/A**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 3 features:



1.  **How is OWASP Dependency Check used?** Not used
1.  **Package managers:** None
1.  **What is the threshold for OWASP Dependency Check?** Not specified


### LGTM score

Score: **0.0**, confidence is 5.0 (low), weight is 1.0 (high)





This sub-score is based on 2 features:



1.  **Programming languages:** PYTHON, GO, OTHER
1.  **The worst LGTM grade of the project:** unknown


### How a project uses CodeQL

Score: **0.0**, confidence is 10.0 (max), weight is 1.0 (high)





This sub-score is based on 4 features:



1.  **Does it run CodeQL scans?** No
1.  **Does it use CodeQL checks for pull requests?** No
1.  **Does it use LGTM checks?** No
1.  **Programming languages:** PYTHON, GO, OTHER


### FindSecBugs score

Score: **N/A**, confidence is 10.0 (max), weight is 0.5 (medium)





This sub-score is based on 2 features:



1.  **Does it use FindSecBugs?** No
1.  **Programming languages:** PYTHON, GO, OTHER


## Known vulnerabilities

1.  [CVE-1999-0384](https://nvd.nist.gov/vuln/detail/CVE-1999-0384): The Forms 2.0 ActiveX control (included with Visual Basic for Applications 5.0) can be used to read text from a user's clipboard when the user accesses documents with ActiveX content.
1.  [CVE-2000-0419](https://nvd.nist.gov/vuln/detail/CVE-2000-0419): The Office 2000 UA ActiveX Control is marked as "safe for scripting," which allows remote attackers to conduct unauthorized activities via the "Show Me" function in Office Help, aka the "Office 2000 UA Control" vulnerability.
1.  [CVE-2002-0727](https://nvd.nist.gov/vuln/detail/CVE-2002-0727): The Host function in Microsoft Office Web Components (OWC) 2000 and 2002 is exposed in components that are marked as safe for scripting, which allows remote attackers to execute arbitrary commands via the setTimeout method.
1.  [CVE-2003-0347](https://nvd.nist.gov/vuln/detail/CVE-2003-0347): Heap-based buffer overflow in VBE.DLL and VBE6.DLL of Microsoft Visual Basic for Applications (VBA) SDK 5.0 through 6.3 allows remote attackers to execute arbitrary code via a document with a long ID parameter.
1.  [CVE-2004-0200](https://nvd.nist.gov/vuln/detail/CVE-2004-0200): Buffer overflow in the JPEG (JPG) parsing engine in the Microsoft Graphic Device Interface Plus (GDI+) component, GDIPlus.dll, allows remote attackers to execute arbitrary code via a JPEG image with a small JPEG COM field length that is normalized to a large integer length before a memory copy operation.
1.  [CVE-2004-0848](https://nvd.nist.gov/vuln/detail/CVE-2004-0848): Buffer overflow in Microsoft Office XP allows remote attackers to execute arbitrary code via a link with a URL file location containing long inputs after (1) "%00 (null byte) in .doc filenames or (2) "%0a" (carriage return) in .rtf filenames.
1.  [CVE-2005-0650](https://nvd.nist.gov/vuln/detail/CVE-2005-0650): Multiple cross-site scripting (XSS) vulnerabilities in ProjectBB 0.4.5.1 allow remote attackers to inject arbitrary web script or HTML via (1) the pages parameter to divers.php (incorrectly referred to as "drivers.php" by some sources), (2) in the search feature text area, (3) forum name, (4) site name or (5) the maximum avatar size in the option section, (5) new category or (6) new forum fields in the forum section.
1.  [CVE-2005-2127](https://nvd.nist.gov/vuln/detail/CVE-2005-2127): Microsoft Internet Explorer 5.01, 5.5, and 6 allows remote attackers to cause a denial of service (application crash) and possibly execute arbitrary code via a web page with embedded CLSIDs that reference certain COM objects that are not intended for use within Internet Explorer, as originally demonstrated using the (1) DDS Library Shape Control (Msdds.dll) COM object, and other objects including (2) Blnmgrps.dll, (3) Ciodm.dll, (4) Comsvcs.dll, (5) Danim.dll, (6) Htmlmarq.ocx, (7) Mdt2dd.dll (as demonstrated using a heap corruption attack with uninitialized memory), (8) Mdt2qd.dll, (9) Mpg4ds32.ax, (10) Msadds32.ax, (11) Msb1esen.dll, (12) Msb1fren.dll, (13) Msb1geen.dll, (14) Msdtctm.dll, (15) Mshtml.dll, (16) Msoeacct.dll, (17) Msosvfbr.dll, (18) Mswcrun.dll, (19) Netshell.dll, (20) Ole2disp.dll, (21) Outllib.dll, (22) Psisdecd.dll, (23) Qdvd.dll, (24) Repodbc.dll, (25) Shdocvw.dll, (26) Shell32.dll, (27) Soa.dll, (28) Srchui.dll, (29) Stobject.dll, (30) Vdt70.dll, (31) Vmhelper.dll, and (32) Wbemads.dll, aka a variant of the "COM Object Instantiation Memory Corruption vulnerability."
1.  [CVE-2006-3864](https://nvd.nist.gov/vuln/detail/CVE-2006-3864): Unspecified vulnerability in mso.dll in Microsoft Office 2000, XP, and 2003, and Microsoft PowerPoint 2000, XP, and 2003, allows remote user-assisted attackers to execute arbitrary code via a malformed record in a (1) .DOC, (2) .PPT, or (3) .XLS file that triggers memory corruption, related to an "array boundary condition" (possibly an array index overflow), a different vulnerability than CVE-2006-3434, CVE-2006-3650, and CVE-2006-3868.
1.  [CVE-2007-0505](https://nvd.nist.gov/vuln/detail/CVE-2007-0505): Unrestricted file upload vulnerability in the Project issue tracking 4.7.0 through 5.x before 20070123, a module for Drupal, allows remote authenticated users to execute arbitrary code by attaching a file with executable or multiple extensions to a project issue.
1.  [CVE-2007-0671](https://nvd.nist.gov/vuln/detail/CVE-2007-0671): Unspecified vulnerability in Microsoft Excel 2000, XP, 2003, and 2004 for Mac, and possibly other Office products, allows remote user-assisted attackers to execute arbitrary code via unknown attack vectors, as demonstrated by Exploit-MSExcel.h in targeted zero-day attacks.
1.  [CVE-2007-4436](https://nvd.nist.gov/vuln/detail/CVE-2007-4436): The Drupal Project module before 5.x-1.0, 4.7.x-2.3, and 4.7.x-1.3 and Project issue tracking module before 5.x-1.0, 4.7.x-2.4, and 4.7.x-1.4 do not properly enforce permissions, which allows remote attackers to (1) obtain sensitive via the Tracker Module and the Recent posts page; (2) obtain project names via unspecified vectors; (3) obtain sensitive information via the statistics pages; and (4) read CVS project activity.
1.  [CVE-2008-1088](https://nvd.nist.gov/vuln/detail/CVE-2008-1088): Microsoft Project 2000 Service Release 1, 2002 SP1, and 2003 SP2 allows user-assisted remote attackers to execute arbitrary code via a crafted Project file, related to improper validation of "memory resource allocations."
1.  [CVE-2008-4253](https://nvd.nist.gov/vuln/detail/CVE-2008-4253): The FlexGrid ActiveX control in Microsoft Visual Basic 6.0, Visual FoxPro 8.0 SP1 and 9.0 SP1 and SP2, Office FrontPage 2002 SP3, and Office Project 2003 SP3 does not properly handle errors during access to incorrectly initialized objects, which allows remote attackers to execute arbitrary code via a crafted HTML document, related to corruption of the "system state," aka "FlexGrid Control Memory Corruption Vulnerability."
1.  [CVE-2009-3126](https://nvd.nist.gov/vuln/detail/CVE-2009-3126): Integer overflow in GDI+ in Microsoft Internet Explorer 6 SP1, Windows XP SP2 and SP3, Office XP SP3, Office 2003 SP3, 2007 Microsoft Office System SP1 and SP2, Office Project 2002 SP1, Visio 2002 SP2, Office Word Viewer, Word Viewer 2003 Gold and SP3, Office Excel Viewer 2003 Gold and SP3, Office Excel Viewer, Office PowerPoint Viewer 2007 Gold, SP1, and SP2, Office Compatibility Pack for Word, Excel, and PowerPoint 2007 File Formats SP1 and SP2, Expression Web, Expression Web 2, Groove 2007 Gold and SP1, Works 8.5, SQL Server 2000 Reporting Services SP2, SQL Server 2005 SP2 and SP3, Report Viewer 2005 SP1, Report Viewer 2008 Gold and SP1, and Forefront Client Security 1.0 allows remote attackers to execute arbitrary code via a crafted PNG image file, aka "GDI+ PNG Integer Overflow Vulnerability."
1.  [CVE-2015-2503](https://nvd.nist.gov/vuln/detail/CVE-2015-2503): Microsoft Access 2007 SP3, Excel 2007 SP3, InfoPath 2007 SP3, OneNote 2007 SP3, PowerPoint 2007 SP3, Project 2007 SP3, Publisher 2007 SP3, Visio 2007 SP3, Word 2007 SP3, Office 2007 IME (Japanese) SP3, Access 2010 SP2, Excel 2010 SP2, InfoPath 2010 SP2, OneNote 2010 SP2, PowerPoint 2010 SP2, Project 2010 SP2, Publisher 2010 SP2, Visio 2010 SP2, Word 2010 SP2, Pinyin IME 2010, Access 2013 SP1, Excel 2013 SP1, InfoPath 2013 SP1, OneNote 2013 SP1, PowerPoint 2013 SP1, Project 2013 SP1, Publisher 2013 SP1, Visio 2013 SP1, Word 2013 SP1, Excel 2013 RT SP1, OneNote 2013 RT SP1, PowerPoint 2013 RT SP1, Word 2013 RT SP1, Access 2016, Excel 2016, OneNote 2016, PowerPoint 2016, Project 2016, Publisher 2016, Visio 2016, Word 2016, Skype for Business 2016, and Lync 2013 SP1 allow remote attackers to bypass a sandbox protection mechanism and gain privileges via a crafted web site that is accessed with Internet Explorer, as demonstrated by a transition from Low Integrity to Medium Integrity, aka "Microsoft Office Elevation of Privilege Vulnerability."
1.  [CVE-2018-8575](https://nvd.nist.gov/vuln/detail/CVE-2018-8575): A remote code execution vulnerability exists in Microsoft Project software when it fails to properly handle objects in memory, aka "Microsoft Project Remote Code Execution Vulnerability." This affects Microsoft Project, Office 365 ProPlus, Microsoft Project Server.
1.  [CVE-2019-1264](https://nvd.nist.gov/vuln/detail/CVE-2019-1264): A security feature bypass vulnerability exists when Microsoft Office improperly handles input, aka 'Microsoft Office Security Feature Bypass Vulnerability'.
1.  [CVE-2020-0760](https://nvd.nist.gov/vuln/detail/CVE-2020-0760): A remote code execution vulnerability exists when Microsoft Office improperly loads arbitrary type libraries, aka 'Microsoft Office Remote Code Execution Vulnerability'. This CVE ID is unique from CVE-2020-0991.
1.  [CVE-2020-1322](https://nvd.nist.gov/vuln/detail/CVE-2020-1322): An information disclosure vulnerability exists when Microsoft Project reads out of bound memory due to an uninitialized variable, aka 'Microsoft Project Information Disclosure Vulnerability'.



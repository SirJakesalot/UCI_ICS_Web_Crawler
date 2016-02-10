Assumptions:

1. Since it was impossible to anticipate all incorrect file extensions, we assumed that it is okay to have the longest page be file. Otherwise, we could have added the file extensions to avoid that.

2. Blacklisted domains were wics, duttgroup, drzaius, ftp, kdd, calendar due to spider traps. Therefore total subdomains found was actually 65 + 6 (blacklisted) = 71.

3. Any valid word only has characters, no numbers or whitespaces. Words with apostraphes or dashs or underscores were parsed to separate words. Example: don't becomes 'don' and 't'.

Compiling

    make

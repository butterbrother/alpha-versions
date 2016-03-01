#include <stdio.h>
#include <stdlib.h>
#include <procfs.h>
#include <limits.h>

#define PR_FILEZISE     100
#define FBUF_SIZE       16384

/* Преобразует 16-битную последовательность во float */
float convertFixedPtBinary(unsigned short FixedPtBin);

/* Парсинг аргументов командной строки */
int parseCmdLine(int argc, char **argv, int *use_uid, unsigned int *uid);

int main(int argc, char **argv) {

        int use_uid;
        unsigned int uid = 0;
        unsigned long pmap_total, pmap_writable;
        int mpflags;

        if (! parseCmdLine(argc, argv, &use_uid, &uid)) return 1;

        char *fname = malloc(sizeof(char) * PR_FILEZISE);
        char *fbuf = malloc(sizeof(char) * FBUF_SIZE);
        FILE *info, *pmapf;
        size_t readed;

        psinfo_t *psinfo = (psinfo_t *) malloc(sizeof(psinfo));
        prmap_t *pmap = (prmap_t *) malloc(sizeof(prmap_t));

        while (*++argv > 0) {
                sprintf(fname, "/proc/%s/psinfo", *argv);
                info = fopen(fname, "rb");

                if (info) {
                        readed = fread(psinfo, sizeof(*psinfo), 1, info);

                        if (!ferror(info) && readed &&
                                        ((use_uid && uid == psinfo->pr_uid) || !use_uid)) {

                                printf("Process ID:\t\t\t%d\n", psinfo->pr_pid);
                                printf("Process user ID:\t\t%d\n", psinfo->pr_uid);
                                printf("Exec file: \t\t\t%s\n", psinfo->pr_fname);
                                printf("Exec arguments: %s\n", psinfo->pr_psargs);
                                printf("Resident set size:\t\t%ld KB.\n", psinfo->pr_rssize);
                                printf("Recent cpu time used:\t\t%f %%\n", convertFixedPtBinary(psinfo->pr_pctcpu));
                                printf("System memory used by process:\t%f %%\n", convertFixedPtBinary(psinfo->pr_pctmem));

                                sprintf(fname, "/proc/%s/map", *argv);
                                pmapf = fopen(fname, "rb");

                                if (pmapf) {
                                        setvbuf(pmapf, fbuf, _IOFBF, FBUF_SIZE);
                                        pmap_total = 0;
                                        pmap_writable = 0;

                                        while ((readed = fread(pmap, sizeof(prmap_t), 1, pmapf)) >= 1) {
                                                mpflags = pmap -> pr_mflags;
                                                if (mpflags & MA_WRITE == MA_WRITE)
                                                        pmap_writable += (unsigned long)pmap -> pr_size;
                                                pmap_total += (unsigned long)pmap -> pr_size;

                                                if (feof(pmapf)) break;
                                        }

                                        printf("Pmap objects size:\t\t%lu B.\n", pmap_total);
                                        printf("Pmap writable objects size:\t%lu B.\n", pmap_writable);

                                        fclose(pmapf);
                                }
                        }

                        fclose(info);
                }

                if (*argv > 0) printf("----------------------\n");
        }

        free(fname);
        free(psinfo);
        free(pmap);
        free(fbuf);
        return 0;
}

int parseCmdLine(int argc, char **argv, int *use_uid, unsigned int *uid) {
        int c;
        *use_uid = 0;
        char *appname = *argv;

        while (--argc > 0 && (*++argv)[0] == '-') {
                c = *++argv[0];
                switch (c) {
                        case 'u':
                                if (--argc > 0 && *++argv != NULL) {
                                        *use_uid = 1;
                                        *uid = atoi(*argv);
                                } else {
                                        printf("User id must be set\n");
                                        return 0;
                                }
                                break;
                        /* Пока не поддерживается. Извлечение uid из имени польователя.
                        case 'U':
                                if (--argc > 0 && *++argv != NULL) {
                                        * Задел на будущее - вытаскивать из /etc/passwd
                                } else {
                                        printf("User name must be set\n");
                                        return 0;
                                }
                                break;
                        */
                        default:
                                printf("Unknown parameter:%c\n", c);
                                return 0;
                                break;
                }
        }

        if (argc < 1) {
                printf("Usage: [-u <use id>] %s pid\n", appname);
                return 0;
        }

        return 1;
}

/* pr_pctcpu and pr_pctmem are 16-bit binary fractions in the range
 * 0.0 to 1.0 with the binary point to the right of the high-order
 * bit (1.0 == 0x8000). pr_pctcpu is the summation over all lwps in
 * the process. */
float convertFixedPtBinary(unsigned short FixedPtBin) {
        float temp = 0.0;
        int bit;

        for (bit = 0; bit < 16; ++bit) {
                temp /= 2.0;
                if (FixedPtBin & 1) temp = temp + 1.0;
                FixedPtBin >>= 1;
        }
        return temp;
}


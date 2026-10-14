## Структура проекта

```text
complex-matrix/
├── .gitignore
├── README.md
├── STUDY_GUIDE.md
├── COMMITS.md
└── src/
    ├── Complex.java
    ├── ComplexMatrix.java
    └── Main.java
```

- `Complex.java` — комплексное число и арифметика.
- `ComplexMatrix.java` — хранение матрицы и операции.
- `Main.java` — ввод, меню и обработка ошибок.
- `STUDY_GUIDE.md` — объяснение кода с нуля, алгоритмов и вопросов для защиты.
- `COMMITS.md` — порядок коммитов и публикация на GitHub.

## Сборка и запуск

```sh
mkdir out
javac -encoding UTF-8 -d out src/Complex.java src/ComplexMatrix.java src/Main.java
java -cp out Main
```



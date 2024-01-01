1. Criação de um exame

POST /exam

{
  "examName": "teste",
  "examUC": "UC de teste",
  "examAdmissionTime": "03:33",
  "examScheduleIDs": []
}

2. Lista de todos os exames

GET /getExams

3. Buscar um ExamHeader de um exame específico

GET /getExamHeader?examName=teste

4. Alterar o ExamHeader de um exame específico

PUT /exams/editExamHeader?examName=teste

5. Criação de uma versão de exame

POST /exam/versions?examID=xxxx

6. Criação de uma questão de escolha múltipla

POST /exams/QuestionMultipleChoice?examName=teste

{
  "question": "pergunta escolha multipla",
  "qn": 2,
  "versionNumber": 1,
  "choices": [{
    "desc": "descrição",
    "correc": true,
    "score": 10,
    "choiceNumber": 1
  }]
}

7. Criação de uma questão de Writing

POST /exams/QuestionWriting?examName=teste

{
  "question": "pergunta writing",
  "qn": 1,
  "versionNumber": 1,
  "criteria": "Critério da pergunta",
  "min": 10,
  "max": 100
}

8. Criação de uma questão de CompleteSpaces

POST /exams/QuestionCompleteSpaces?examName=teste

{
  "question": "pergunta CompleteSpaces",
  "qn": 3,
  "versionNumber": 1,
  "text": "Hoje é o dia {[antes, depois], 3} da {[ceia], 3} de Natal"
}

9. Criação de uma questão de TrueOrFalse

POST /exams/QuestionTrueorFalse?examName=teste

{
  "question": "pergunta TrueOrFalse",
  "qn": 4,
  "versionNumber": 1,
  "questions": [
    {
      "desc": "Primeira TOFQ",
      "correc": true,
      "score": 30,
      "optionNumber": 1
    }
  ]
}

10. Inscrição de estudantes num  exame

PUT /enrollStudents?examName=teste

[
  "44444444-4444-4444-4444-444444444444"
]

11. Criar a reposta a um exame de um aluno

POST /exam/createExamAnswer?examName=teste

{
  "studentID": "44444444-4444-4444-4444-444444444444",
  "grade": 20
}

12. Adicionar as respostas do aluno a uma reposta a um exame (CompleteSpacesAnswer)

POST /exam/saveCompleteSpacesAnswer/teste/1/3/44444444-4444-4444-4444-444444444444

{
  "grade": 5,
  "answer": "Hoje é o dia {antes} da {ceia} de Natal"
}

13. Adicionar as respostas do aluno a uma reposta a um exame (WritingAnswer)

POST /exam/saveWritingAnswer/teste/1/1/44444444-4444-4444-4444-444444444444

{
  "grade": 10,
  "text": "A resposta certa é aquela que não está errada"
}

14. Adicionar as respostas do aluno a uma reposta a um exame (TrueOrFalseAnswer)

POST /exam/saveTrueOrFalseAnswer/teste/1/4/44444444-4444-4444-4444-444444444444

{
  "grade": 2,
  "answers": [
    {
      "grade": 2,
      "answer": true,
      "optionNumber": 1
    }
  ]
}

15. Adicionar as respostas do aluno a uma reposta a um exame (MultipleChoiceAnswer)

POST /exam/saveMultipleChoiceAnswer/teste/1/2/44444444-4444-4444-4444-444444444444

{
  "grade": 3,
  "choices": [
    {
      "grade": 3,
      "selected": false,
      "choiceNumber": 1
    }
  ]
}

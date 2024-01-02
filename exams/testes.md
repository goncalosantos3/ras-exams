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

GET /getExamHeader?examID=xxxx

4. Alterar o ExamHeader de um exame específico

PUT /exams/editExamHeader?examID=xxxx

5. Criação de uma versão de exame

POST /exam/versions?examID=xxxx

6. Eliminação da versão de um exame

DELETE POST /exam/versions?examID=xxxx&versionID=xxxx 

7. Criação de uma questão de escolha múltipla

POST /exam/QuestionMultipleChoice?examID=xxxx

{
  "question": "pergunta escolha multipla",
  "qn": 2,
  "versionID": xxxx,
  "choices": [{
    "desc": "descrição",
    "correc": true,
    "score": 10,
    "choiceNumber": 1
    },{
      "desc": "descrição2",
      "correc": false,
      "score": 30,
      "choiceNumber": 2
    }
  ]
}

8. Criação de uma questão de Writing

POST /exam/QuestionWriting?examID=xxxx

{
  "question": "pergunta writing",
  "qn": 1,
  "score": 40,
  "versionID": xxxx,
  "criteria": "Critério da pergunta",
  "min": 10,
  "max": 100
}

9. Criação de uma questão de CompleteSpaces

POST /exam/QuestionCompleteSpaces?examID=xxxx

{
  "question": "pergunta CompleteSpaces",
  "qn": 3,
  "versionID": xxxx,
  "text": "Hoje é o dia {[antes, depois], 10} da {[ceia], 20} de Natal"
}

10. Criação de uma questão de TrueOrFalse

POST /exam/QuestionTrueorFalse?examID=xxxx

{
  "question": "pergunta TrueOrFalse",
  "qn": 4,
  "versionID": xxxx,
  "questions": [
    {
      "desc": "Primeira TOFQ",
      "correc": true,
      "score": 30,
      "optionNumber": 1
    }, 
    {
      "desc": "Segunda TOFQ",
      "correc": false,
      "score": 40,
      "optionNumber": 2
    }
  ]
}

11. Inscrição de estudantes num exame

POST /enrollStudents?examID=xxxx

[
  "44444444-4444-4444-4444-444444444444"
]

12. Adicionar as respostas do aluno a uma reposta a um exame (CompleteSpacesAnswer)

POST /exam/saveCompleteSpacesAnswer/{examID}/{versionID}/{questionNumber}/{studentID}

{
  "answer": "Hoje é o dia {antes} da {tarde} de Natal"
}

13. Adicionar as respostas do aluno a uma reposta a um exame (WritingAnswer)

POST /exam/saveWritingAnswer/{examID}/{versionID}/{questionNumber}/{studentID}

{
  "text": "A resposta certa é aquela que não está errada"
}

14. Adicionar as respostas do aluno a uma reposta a um exame (TrueOrFalseAnswer)

POST /exam/saveTrueOrFalseAnswer/{examID}/{versionID}/{questionNumber}/{studentID}

{
  "answers": [
    {
      "answer": true,
      "optionNumber": 1
    },
    {
      "answer": true,
      "optionNumber": 2
    }
  ]
}

15. Adicionar as respostas do aluno a uma reposta a um exame (MultipleChoiceAnswer)

POST /exam/saveMultipleChoiceAnswer/{examID}/{versionID}/{questionNumber}/{studentID}

{
  "choices": [
    {
      "selected": false,
      "choiceNumber": 1
    },
    {
      "selected": true,
      "choiceNumber": 2
    }
  ]
}

16. Corrigir automaticamente um exame

POST /exam/autoCorrect?examID=xxxx
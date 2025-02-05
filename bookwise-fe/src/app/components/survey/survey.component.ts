import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-survey',
  templateUrl: './survey.component.html',
  styleUrls: ['./survey.component.css'],
})
export class SurveyComponent implements OnInit {
  questions: any[] = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadQuestions();
  }

  loadQuestions(): void {
    this.http.get<any[]>('http://localhost:8080/book-wise/api/questions').subscribe({
      next: (data) => {
        this.questions = data.map((q) => ({ ...q, answer: null }));
      },
      error: (error) => {
        console.error('Error al cargar las preguntas:', error);
      },
    });
  }

  submitSurvey(): void {
    const email = localStorage.getItem('userEmail');
    if (email) {
      const surveyResponse = this.questions.map((q) => ({

        surveyId: 'someSurveyId',
        responses: [
          {
            questionId: q.id,
            selectedOption: {
              answer: q.answer,
              weight: q.weight,
            },
          },
        ],
      }));

      this.http.post(`http://localhost:8080/book-wise/api/user/survey?email=${email}`, surveyResponse, { responseType: 'text' })
      .subscribe({
      next: (preferredGenre: string) => {
      this.savePreferences(preferredGenre);
        alert(`Encuesta guardada con éxito. Tu género preferido es: ${preferredGenre}`);
        this.router.navigate(['/home']);
      },
      error: (err) => {
      console.error('Error al guardar la encuesta:', err);
      },
    });


    }
  }

  savePreferences(preferredGenre: string): void {
    localStorage.setItem('userPreferredGenre', preferredGenre);
  }

  goBack(): void {
    window.history.back();
  }

}

import { HttpClient } from '@angular/common/http'; // Import HttpClient
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router'; // To navigate to another page after successful signup

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule,RouterModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit {
  signupForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.signupForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(2)]],
    });
  }

  onSubmit(): void {
    if (this.signupForm.valid) {
      const formData = this.signupForm.value;
      this.http.post('http://localhost:8888/api/signup', formData).subscribe({
        next: (response) => {
          alert(response.toString() + 'Account saved successfully');
          this.router.navigate(['/login']); // Redirect to login page on success
        },
        error: (error) => {
          console.error('Signup error', error);
          alert(error.error); // Show error message from backend
        },
      });
    } else {
      alert('Form is invalid');
    }
  }
}

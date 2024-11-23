import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string = '';
  successMessage: string = '';

  private apiUrl = 'http://localhost:8888/api/login'; // Backend API URL

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const loginData = this.loginForm.value;

      // Log the data being sent
      console.log('Login data being sent:', loginData);

      // Make the HTTP POST request to the backend
      this.http.post<any>(this.apiUrl, loginData).subscribe(
        (response) => {
          // Log the backend response
          console.log('Response from backend:', response);

          // Check if the response is successful and contains a message
          if (response && response.message) {
            this.successMessage = response.message;
            alert('Login successful!'); // Display success message
            localStorage.setItem('loggedIn', 'true');
            localStorage.setItem('username', loginData.username); // Save the username

            this.router.navigate(['/payment']); // Navigate to dashboard or another page
          } else {
            this.successMessage = 'Unexpected response format.';
            alert(this.successMessage);
          }
        },
        (error) => {
          // Log any error from the backend
          console.error('Error from backend:', error);
          if (error.error && error.error.message) {
            this.errorMessage = error.error.message; // Set the error message from the response
          } else {
            this.errorMessage = 'Login failed. Please try again.';
          }
          alert(this.errorMessage); // Show the error message to the user
        }
      );
    }
  }
}

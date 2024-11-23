import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { loadStripe } from '@stripe/stripe-js';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, RouterModule, NavbarComponent],
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css'],
})
export class PaymentComponent implements OnInit {
  loginusername: string | null = null; // Store the username from localStorage
  stripe: any;
  elements: any;
  amount!: number;
  currency: string = 'USD'; // Default currency
  cardElement: any;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loginusername = localStorage.getItem('username');
    console.log(this.loginusername);
    //  if (!this.loginusername) {
    //    alert('User not logged in. Redirecting to login page.');
    //    this.router.navigate(['/login']); // Redirect to login if not logged in
    //    return;
    //  }
    loadStripe(
      'pk_test_51QLO04F82qzYQYpBG6hyQSxdPSUljqKCdzLiX4ipBa2fJUtSSq372JMMKUQC4Tcvu2zrByWQzkzdnF0L7cxQhGT300DGqlSnN7'
    ).then((stripe: any) => {
      this.stripe = stripe;
      this.elements = stripe.elements();
      this.cardElement = this.elements.create('card');
      this.cardElement.mount('#card-element');
    });
  }

  onSubmit() {
    this.createPaymentToken().then((paymentToken) => {
      if (paymentToken) {
        const transactionData = {
          amount: this.amount.toString(),
          currency: this.currency,
          paymentToken: paymentToken,
          loginusername: this.loginusername,
        };

        this.http
          .post('http://localhost:8888/api/processPayment', transactionData)
          .subscribe(
            (response: any) => {
              alert('Payment successful!' + response.status);
              console.log(this.loginusername);
            },
            (error) => {
              console.error('Payment failed', error);
              alert('Payment failed. Please try again.');
            }
          );
      }
    });
  }

  createPaymentToken(): Promise<string | null> {
    return new Promise((resolve, reject) => {
      this.stripe.createToken(this.cardElement).then((result: any) => {
        if (result.error) {
          alert(result.error.message);
          reject(null);
        } else {
          resolve(result.token.id);
        }
      });
    });
  }
}

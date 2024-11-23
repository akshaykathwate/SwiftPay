import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from '../navbar/navbar.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [CommonModule, NavbarComponent, ReactiveFormsModule, FormsModule],
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css'],
})
export class TransactionsComponent implements OnInit {
  transactions: any[] = [];
  loading: boolean = false;
  errorMessage: string | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchTransactions();
  }

  fetchTransactions(): void {
    this.loading = true;
    this.errorMessage = null;

    const username = localStorage.getItem('username');
    if (!username) {
      this.errorMessage = 'Username not found in local storage.';
      this.loading = false;
      return;
    }

    this.http
      .get<any[]>(
        `http://localhost:8888/api/transactions/by-username?username=${username}`
      )
      .subscribe(
        (data) => {
          console.log('Data received from backend:', data); // Logs the data to the console

          this.transactions = data.map((transaction) => ({
            transactionId: transaction.transactionId,
            date: transaction.date,
            amount: transaction.amount,
            currency: transaction.currency,
            message: transaction.message,
            status: transaction.status,
          }));

          this.loading = false;
        },
        (error) => {
          console.error('Error fetching transactions:', error);
          this.errorMessage =
            'Unable to fetch transactions. Please try again later.';
          this.loading = false;
        }
      );
  }

  viewTransaction(transaction: any): void {
    // Handle view logic
    alert(`Viewing transaction: ${transaction.transactionId}`);
  }

  deleteTransaction(transactionId: number): void {
    // Handle delete logic
    alert(`Deleting transaction with ID: ${transactionId}`);
  }
}

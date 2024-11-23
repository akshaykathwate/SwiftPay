import { Routes } from '@angular/router';
import { AuthGuard } from './auth.guard'; // Import the guard
import { LoginComponent } from './login/login.component';
import { PaymentComponent } from './payment/payment.component'; // Assuming PaymentComponent exists
import { SignupComponent } from './signup/signup.component';
import { TransactionsComponent } from './transactions/transactions.component';

export const routes: Routes = [
  { path: 'signup', component: SignupComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'payment', component: PaymentComponent, canActivate: [AuthGuard] },
  {
    path: 'transactions',
    component: TransactionsComponent,
    canActivate: [AuthGuard],
  },
];

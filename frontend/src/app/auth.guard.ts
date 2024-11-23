import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> | Promise<boolean> | boolean {
    // Check for browser environment and localStorage
    const isBrowser = typeof window !== 'undefined';

    if (isBrowser) {
      const isLoggedIn = localStorage.getItem('loggedIn') === 'true'; // Example check

      if (!isLoggedIn) {
        // Redirect to login page if not logged in
        this.router.navigate(['/login']);
        return false;
      }
      return true;
    }

    // For SSR or non-browser environments, deny access
    return false;
  }
}

// import { Injectable } from '@angular/core';
// import {
//   CanActivate,
//   ActivatedRouteSnapshot,
//   RouterStateSnapshot,
//   Router,
// } from '@angular/router';
// import { Observable } from 'rxjs';

// @Injectable({
//   providedIn: 'root',
// })
// export class AuthGuard implements CanActivate {
//   constructor(private router: Router) {}

//   canActivate(
//     next: ActivatedRouteSnapshot,
//     state: RouterStateSnapshot
//   ): Observable<boolean> | Promise<boolean> | boolean {
//     // Check if the user is logged in (you can use local storage, a service, etc.)
//     const isLoggedIn = localStorage.getItem('loggedIn') === 'true'; // Example check

//     if (!isLoggedIn) {
//       // Redirect to login page if not logged in
//       this.router.navigate(['/login']);
//       return false;
//     }
//     return true;
//   }
// }

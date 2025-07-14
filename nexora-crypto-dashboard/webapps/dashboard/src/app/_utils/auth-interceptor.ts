import { HttpInterceptorFn } from '@angular/common/http';
import { HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenStorageService } from '../_services/tokenStorageService';
import { AuthService } from '../_services/AuthService';
import {
  BehaviorSubject,
  Observable,
  catchError,
  filter,
  switchMap,
  take,
  throwError
} from 'rxjs';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const AuthInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> => {
  const tokenStorage = inject(TokenStorageService);
  const authService = inject(AuthService);

  const isRefreshUrl = req.url.includes('/auth/refresh-token');
  const accessToken = tokenStorage.getToken();

  if (accessToken && !isRefreshUrl) {
    req = addTokenHeader(req, accessToken);
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401 && !isRefreshUrl) {
        return handle401Error(req, next, tokenStorage, authService);
      }
      return throwError(() => error);
    })
  );
};

function addTokenHeader(request: HttpRequest<any>, token: string): HttpRequest<any> {
  return request.clone({
    headers: request.headers.set('Authorization', `Bearer ${token}`)
  });
}

function handle401Error(
  request: HttpRequest<any>,
  next: HttpHandlerFn,
  tokenStorage: TokenStorageService,
  authService: AuthService
): Observable<HttpEvent<any>> {
  if (!isRefreshing) {
    isRefreshing = true;
    refreshTokenSubject.next(null);

    return authService.refreshToken().pipe(
      switchMap((tokens: any) => {
        tokenStorage.saveToken(tokens.accessToken);
        tokenStorage.saveRefreshToken(tokens.refreshToken);
        refreshTokenSubject.next(tokens.accessToken);
        isRefreshing = false;

        return next(addTokenHeader(request, tokens.accessToken));
      }),
      catchError(err => {
        isRefreshing = false;
        refreshTokenSubject.next(null);
        tokenStorage.clear();
        return throwError(() => err);
      })
    );
  } else {
    return refreshTokenSubject.pipe(
      filter(token => token !== null),
      take(1),
      switchMap(token => next(addTokenHeader(request, token!)))
    );
  }
}


// import { inject } from '@angular/core';
// import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
// import { TokenStorageService } from '../_services/tokenStorageService';
// import { AuthService } from '../_services/AuthService';
// import { catchError, switchMap, throwError } from 'rxjs';

// export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
//   const tokenStorage = inject(TokenStorageService);
//   const authService = inject(AuthService);

//   const isRefreshUrl = req.url.includes('/auth/refresh-token');

//   if (!isRefreshUrl) {
//     const accessToken = tokenStorage.getToken();
//     if (accessToken) {
//       req = req.clone({
//         headers: req.headers.set('Authorization', `Bearer ${accessToken}`),
//       });
//     }
//   }

//   return next(req).pipe(
//     catchError((error: HttpErrorResponse) => {
//       if (error.status === 401 && !isRefreshUrl) {
//         return authService.refreshToken().pipe(
//           switchMap((tokens: any) => {
//             console.log('Tokens received from backend:', tokens);

//             tokenStorage.saveToken(tokens.accessToken);
//             console.log('Access token saved:', tokens.accessToken);

//             tokenStorage.saveRefreshToken(tokens.refreshToken);
//             console.log('Refresh token saved:', tokens.refreshToken);

//             const clonedReq = req.clone({
//               headers: req.headers.set('Authorization', `Bearer ${tokens.accessToken}`),
//             });

//             return next(clonedReq);
//           }),
//           catchError(refreshError => {
//             tokenStorage.clear();
//             return throwError(() => refreshError);
//           })
//         );
//       }
//       return throwError(() => error);
//     })
//   );
// };

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';
import { SignupRequestPayload } from '../../../components/auth/signup/signup-request.payload';
import { LoginRequestPayload } from '../../../components/auth/login/login-request.payload';
import { LoginResponse } from '../../../components/auth/login/login-response';
import { LocalStorageService } from 'ngx-webstorage';
import { map, tap } from 'rxjs/operators';
import { ThrowStmt } from '@angular/compiler';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient, private localStorage: LocalStorageService) { }

  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.http.post('http://localhost:8080/api/auth/signup', signupRequestPayload, {
      responseType: 'text'
    })
  }

  login(LoginRequestPayload: LoginRequestPayload): Observable<boolean> {
    return this.http.post<LoginResponse>('http://localhost:8080/api/auth/login', LoginRequestPayload)
    .pipe(map(data => {
      this.localStorage.store('authenticationToken', data.authenticationToken);
      this.localStorage.store('username', data.username);
      this.localStorage.store('expiresAt', data.expiresAt);
      this.localStorage.store('refreshToken', data.refreshToken);
      return true;
    }))
  }

  refreshToken() {
    const refreshTokenPayload = {
      refreshToken: this.getRefreshToken(),
      username: this.getUsername()
    }

    return this.http.post<LoginResponse>('http://localhost:8080/api/auth/refresh/token', refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorage.store('authenticationToken', response.authenticationToken)
        this.localStorage.store('expiresAt', response.expiresAt);
        
      }))
  }

  getJwtToken() {
    return this.localStorage.retrieve('authenticationToken')
  }

  getRefreshToken() {
    return this.localStorage.retrieve('refreshToken')
  }

  getUsername() {
    return this.localStorage.retrieve('username')
  }

  getExpirationTime() {
    return this.localStorage.retrieve('expiresAt')
  }
}



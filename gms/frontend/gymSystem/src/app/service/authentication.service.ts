import { Injectable } from '@angular/core';
import { environment} from "../../environments/environment";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/user_model"
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private host = environment.apuUrl
  private token: string | null = null;
  private loggedInUsername: string | null = null;
  private jwtHepler = new JwtHelperService();

  constructor(private http: HttpClient) { }

  public login(user: User): Observable<HttpResponse<any> | HttpErrorResponse>{
    return this.http.post<HttpResponse<any> | HttpErrorResponse>
    (`${this.host}/user/login`, user,{observe: `response`});
  }

  public register(user: User): Observable<User | HttpErrorResponse>{
    return this.http.post<User | HttpErrorResponse>
    (`${this.host}/user/register`, user);
  }
  public logOut(): void{
    this.token = null;
    this.loggedInUsername = null;
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('users');
  }
  public  saveToken(token: string): void{
    this.token = token;
    localStorage.setItem('token',token);
  }
  public  addUserToLocalCache(user: User): void{
    localStorage.setItem('user',JSON.stringify(user));
  }
  public getUserFromLocalCache(): User{
    return JSON.parse(localStorage.getItem('user') || '{}')
  }
  public loadToken(): void{
    this.token = localStorage.getItem('token') || '{}';//unsafe to left token named token xd
  }
  public getToken(): string{
    return this.token || '{}';
  }
  public isLoggedIn(): boolean{
    this.loadToken();
    if(this.token != null && this.token !==''){
      if(this.jwtHepler.decodeToken(this.token).sub != null || ''){
        if(!this.jwtHepler.isTokenExpired(this.token)){
          this.loggedInUsername = this.jwtHepler.decodeToken(this.token).sub;
          return true;
        }
      }
    } else {
      this.logOut();
    }
    return false;
  }
}


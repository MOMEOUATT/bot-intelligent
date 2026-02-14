import { Component, OnInit } from '@angular/core';
import { CommonModule} from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ApiService } from '../../services/api-service';
import { AuthService } from '../../services/auth-service';
import { Route, Router } from '@angular/router';
import { User } from '../../models/user';
import { BotAvatar, EyeState } from '../bot-avatar/bot-avatar';

@Component({
  selector: 'app-auth',
  imports: [CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    BotAvatar],
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})
export class Auth implements OnInit {

  loginForm!: FormGroup;
  signupForm!: FormGroup;

  isSignup = false;
  loading = false;
  hidePassword = true;
  hidePasswordSignup = true;
  isPasswordFocused = false;
  isPasswordVisible = false;

  errorMessage = "";
  successMessage = "";

  constructor(
    private fb: FormBuilder,
    private apiService: ApiService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    
    this.loginForm = this.fb.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required]]
    });

    this.signupForm = this.fb.group({
      email: ["", [Validators.required, Validators.email]],
      username: ["", [Validators.required, Validators.minLength(3)]],
      password: ["", [Validators.required, Validators.minLength(6)]]
    });

    if(this.authService.isLoggedIn()){
      this.router.navigate(["/chat"]);
    }
    
  }

  toggleMode(): void {
    this.isSignup = !this.isSignup;
    this.errorMessage = "";
    this.successMessage = "";
  }

  onLogin(): void {
    if(this.loginForm.invalid) {return;}

    this.loading = true;
    this.errorMessage = "";

    const {email, password} = this.loginForm.value;

    this.apiService.getUserByEmail(email).subscribe({
      next: (user: User) => {

        this.authService.login(user);
        this.router.navigate(["/chat"]);
        this.loading = false;
      },

      error: (error) => {
        console.error("Erreur de connexion: ", error);
        this.errorMessage = "Eamil ou mot de passe incorrect";
        this.loading = false;
      }
    });
  }

  onSignup(): void {
    if(this.signupForm.invalid){return;}

    this.loading = true;
    this.errorMessage = "";
    this.successMessage = "";

    const {email, username, password} = this.signupForm.value;

    this.apiService.createUser(email, username, password).subscribe({
      next: (user: User) => {
        this.successMessage = "Compte créé avec succès!! Connexion en cours.....";

        setTimeout(() => {
          this.authService.login(user);
          this.router.navigate(["/chat"]);
        }, 1500);
        
        this.loading = false;
      },

      error: (error) => {
        console.error("Erreur d'inscription", error);
        
        if(error.status === 400) {
          this.errorMessage = "Cet email est déjà utilisé";
        } else {
          this.errorMessage = "Erreur lors de la création du compte";
        }

        this.loading = false;
      }
    });
  }

  /**
 * Détermine l'état des yeux du bot
 */
  getEyeState(): EyeState {
    if (!this.isPasswordFocused) {
      return 'open';
    }
    
    if (this.hidePassword && !this.hidePasswordSignup) {
      return 'closed';
    }
    
    if (!this.hidePassword || !this.hidePasswordSignup) {
      return 'half';
    }
    
    return 'closed';
  }
}

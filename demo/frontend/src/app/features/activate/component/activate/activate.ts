import { CommonModule } from "@angular/common";
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit } from "@angular/core";
import { ActivateService } from "../../service/activate-service";
import { ActivatedRoute, RouterModule } from "@angular/router";
import { Observable, Subscription } from "rxjs";

@Component({
  selector: "app-activate",
  imports: [CommonModule, RouterModule],
  templateUrl: "./activate.html",
  styleUrl: "./activate.css",
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ActivateComponent implements OnInit, OnDestroy {
  errorMessage: string = '';
  activationState: null | 'success' | 'error' = null;
  isLoading: boolean = false;
  isResending: boolean = false;

  private username: string | null = null;

  private activateSub$ : Observable<any> | null = null;

  constructor(
    private route: ActivatedRoute,
    private activateService: ActivateService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.username = this.route.snapshot.paramMap.get('username');
    if(!this.username)
      return;
    this.activateSub$ = this.activateService.activate(this.username);
  }

  ngOnDestroy(): void {}

  onActivate(): void {
    if (!this.username) {
      this.activationState = 'error';
      this.errorMessage = 'Invalid activation link.';
      return;
    }
    
    this.errorMessage = '';
    this.isLoading = true; // Optional: set loading state before call
    
    this.activate();
  }

  onRetry(): void {
    this.activationState = null;
    this.errorMessage = '';
    this.onResend();
  }

  onResend(): void {
    if (!this.username) return;
    
    this.isLoading = true;
    
    this.activate();
  }

  private activate() {
    this.activateSub$?.subscribe({
      next: () => {
        this.activationState = 'success';
        this.isLoading = false;
        this.cdr.markForCheck(); // <-- 4. Mark for check here too
      },
      error: (err) => {
        this.activationState = 'error';
        this.errorMessage = err?.error?.message ?? 'Activation failed.';
        this.isLoading = false;
        this.cdr.markForCheck();
      }
    });
  }
}
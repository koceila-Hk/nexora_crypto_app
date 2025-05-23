import { Component } from '@angular/core';
import { HeaderComponent } from "../common/header/header.component";
import { FooterComponent } from "../common/footer/footer.component";
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home-not-auth',
  standalone: true,
  imports: [HeaderComponent, FooterComponent, RouterLink],
  templateUrl: './home-not-auth.component.html',
  styleUrl: './home-not-auth.component.css'
})
export class HomeNotAuthComponent {

}

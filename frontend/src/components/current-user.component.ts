import { Component } from '@angular/core';

import { User } from '../models/user';
import { UsersService } from '../users.service';

@Component({
  selector: 'app-current-user',
  template: `
    <div>Logged in as: <strong>{{text}}</strong></div>
`
})

export class CurrentUserComponent {
  text = "X";

  constructor(users: UsersService) {
    console.log(users);
    let userP: Promise<User|null> = users.getUser();
    userP.then(user => {
      this.text = (user == null) ? "UNDEF" : user.name;
    })
  }
}


import { helper } from '@ember/component/helper';

export default Ember.Helper.extend({
  session: Ember.inject.service(),

  compute(params) {
    let session = this.get('session');
    let bids = params[0];
    var checker = false;
    
    bids.forEach((item, index) => {
      if(item.user.get('id') == session.userID){
        checker = true;
      }
    });

    return checker;
  }
});


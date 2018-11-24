import { helper } from '@ember/component/helper';

export default Ember.Helper.extend({
  
  compute(params) {
    let bids = params[0];
    var highestBid = 0.00;
    
    bids.forEach((item, index) => {
      console.log(item.amount)
      if(item.amount > highestBid){
        highestBid = item.amount;
      }
    });

    return highestBid;
  }
});

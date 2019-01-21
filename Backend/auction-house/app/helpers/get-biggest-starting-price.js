import { helper } from '@ember/component/helper';

export default Ember.Helper.extend({
  
  compute(params) {
    let products = params[0];
    var highestPrice = 1.00;
    console.log(products);
    
    products.forEach((item, index) => {
      if(item.startingPrice > highestBid){
        highestPrice = item.amount;
      }
    });

    return highestPrice;
  }
});

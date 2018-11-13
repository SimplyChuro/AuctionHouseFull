import { helper } from '@ember/component/helper';
import ENV from 'auction-house/config/environment';

export function isLogged() {
	var checker = ENV.USER_TOKEN;
	if (checker !== '') {
		 return  true;
	}else{
		return false;
	}
}

export default helper(isLogged);

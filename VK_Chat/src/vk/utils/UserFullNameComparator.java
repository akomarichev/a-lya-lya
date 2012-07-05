package vk.utils;

import java.util.Comparator;

import vk.api.User;

public class UserFullNameComparator implements Comparator<User> {

	@Override
	public int compare(User u1, User u2) {
		return u1.getFullName().compareTo(u2.getFullName());
	}
}
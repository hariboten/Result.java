package com.haribote.app;

public interface Result<T, E> {

	public boolean isOk();

	public boolean isErr();

	public T expect(String string);

	public T unwrap();

}

class Ok<T, E> implements Result<T, E> {
	private final T value;
	public Ok(final T value) {
		this.value = value;
	}
	@Override
	public boolean isOk() {
		return true;
	}
	@Override
	public boolean isErr() {
		return false;
	}
	@Override
	public T expect(String _errorMessage) {
		return this.value;
	}
	@Override
	public T unwrap() {
		return this.value;
	}
}

class Err<T, E> implements Result<T, E> {
	private final E value;
	public Err(final E value) {
		this.value = value;
	}
	@Override
	public boolean isOk() {
		return false;
	}
	@Override
	public boolean isErr() {
		return true;
	}
	@Override
	public T expect(String message) {
		if (value instanceof Throwable e) {
			throw new UnsafeUnwrapException(message + ": ", e);
		}
		var errorMessage = message + ": " + value.toString();
		throw new UnsafeUnwrapException(errorMessage);
	}
	@Override
	public T unwrap() {
		if (value instanceof Throwable e) {
			throw new UnsafeUnwrapException(e);
		}
		throw new UnsafeUnwrapException(value.toString());
	}
}

class UnsafeUnwrapException extends RuntimeException {
	public UnsafeUnwrapException(final String message) {
		super(message);
	}

	public UnsafeUnwrapException(final Throwable e) {
		super(e);
	}

	public UnsafeUnwrapException(final String message, final Throwable e) {
		super(message, e);
	}
}

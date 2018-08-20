package com.personal.segments.stream.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ResultSetSpliterator<T> extends Spliterators.AbstractSpliterator<T>{

	private ResultSet resultSet;
	private Function<ResultSet, T> converter;
	
	public ResultSetSpliterator(ResultSet resultSet, Function<ResultSet, T> converter) {
		super(Long.MAX_VALUE, NONNULL);
		this.resultSet = resultSet;
		this.converter = converter;
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
		try {
			if(resultSet.next()) {
				action.accept(converter.apply(resultSet));
				return true;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return false;
	}
	
	public Stream<T> stream(){
		return StreamSupport.stream(this, true);
	}
}
